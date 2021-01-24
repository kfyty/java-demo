var patchUpload = {
    /**
     * 分片上传成功索引
     */
    succeed: [],

    /**
     * 分片上传失败索引
     */
    failed: [],

    /**
     * 失败重试次数
     */
    try: 3,

    /**
     * 分片大小，这里是 5MB
     */
    shardSize: 5 * 1024 * 1024,

    /**
     * 初始化
     */
    init: function () {
        this.setEvent();
    },

    /**
     * 设置页面事件监听
     */
    setEvent: function () {
        var me = this;
        $("#upload").click(function (e) {
            var files = $("#file")[0].files;
            if(files.length < 1) {
                alert("请选择文件！");
                return;
            }
            me.succeed = [];
            me.failed = [];
            me.try = 3;
            me.loadProcess(0);
            me.md5checkUpload(files[0]);
        });

        $("#try").click(function (e) {
            var files = $("#file")[0].files;
            me.try = 3;
            me.md5checkUpload(files[0]);
        });
    },

    /**
     * 检查文件是否已存在
     * @param file
     * @param md5
     */
    checkUpload: function (file, md5) {
        var me = this;
        $.ajax({
            url: "/file/exists",
            type: "get",
            data: {md5: md5, size: file.size},
            dataType: "json",
            success: function(data) {
                if (data.status === 1) {
                    me.loadProcess(100);
                    alert("急速秒传！");
                    return ;
                }
                if(data.id && data.status === 0) {
                    me.succeed = data.patchIndex;
                    me.upload(data.id, file);
                    return ;
                }
                me.upload(me.prepareUpload(md5, file), file);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert("服务器错误！");
            }
        });
    },

    /**
     * 文件不存在时，插入文件的基本信息，为上传文件分片做准备
     * @param md5
     * @param file
     * @returns {*}
     */
    prepareUpload: function (md5, file) {
        var id;
        $.ajax({
            url: "/file/new",
            type: "post",
            async: false,
            data: JSON.stringify({name: file.name, md5: md5, size: 0}),
            contentType: "application/json;charset=utf-8",
            dataType: "json",
            success: function(data) {
                if(data && data.id) {
                    id = data.id;
                    return;
                }
                alert("上传文件失败！");
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert("服务器错误！");
            }
        });
        return id;
    },

    /**
     * 上传文件
     * @param id
     * @param file
     */
    upload: function (id, file) {
        var me = this;
        if(!id) return;
        var shardCount = Math.ceil(file.size / this.shardSize);
        for (var i = 0; i < shardCount; i++) {
            if(me.succeed.length !== 0 && me.succeed.indexOf(i) > -1 && me.failed.indexOf(i) === -1) {
                continue;
            }
            this.uploadPatch(id, file, i, shardCount);
        }
    },

    /**
     * 上传分片文件
     * @param parent
     * @param file
     * @param index
     * @param shardCount
     */
    uploadPatch: function (parent, file, index, shardCount) {
        var me = this;
        var start = index * this.shardSize;
        var end = Math.min(file.size, start + this.shardSize);
        var patch = file.slice(start, end);
        var spark = new SparkMD5();
        var reader = new FileReader();
        reader.readAsBinaryString(patch);
        $(reader).load(function (e) {
            spark.appendBinary(e.target.result);
            var md5 = spark.end();
            var form = new FormData();
            form.append("index", index);
            form.append("parent", parent);
            form.append("md5", md5);
            form.append("size", patch.size);
            form.append("patch", patch);
            form.append("name", file.name + "-patch-" + index);
            $.ajax({
                url: "/file/patch/upload",
                type: "post",
                data: form,
                processData: false,
                contentType: false,
                dataType: "json",
                success: function(data) {
                    if(!data || !data.ok) {
                        me.failed.push(index);
                        console.log("上传分片" + index + "失败！");
                        return ;
                    }
                    me.succeed.push(index);
                    console.log("上传分片" + index + "成功！");
                    me.loadProcess(((me.succeed.length - 1) * me.shardSize + patch.size) / file.size * 100);
                    me.mergePatch(parent, file, shardCount);
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    me.failed.push(index);
                    console.log("服务器错误，上传分片" + index + "失败！");
                    me.tryAgain(parent, file, shardCount);
                }
            });
        });
    },

    /**
     * 请求合并文件
     * @param parent
     * @param file
     * @param shardCount
     */
    mergePatch: function (parent, file, shardCount) {
        var me = this;
        if(me.succeed.length + me.failed.length !== shardCount) return;
        if(me.failed.length !== 0) {
            me.tryAgain(parent, file, shardCount);
            return ;
        }
        $.ajax({
            url: "/file/patch/merge",
            type: "post",
            data: {parent: parent, size: file.size},
            dataType: "json",
            success: function(data) {
                if (data && data.ok) {
                    me.loadProcess(100);
                    alert("上传文件成功！");
                    return ;
                }
                alert("上传文件失败！");
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                alert("服务器错误！");
            }
        });
    },

    /**
     * 重试
     */
    tryAgain: function (parent, file, shardCount) {
        var me = this;
        if(me.succeed.length + me.failed.length !== shardCount) return;
        if(me.failed.length === 0) {
            me.mergePatch(parent, file, shardCount);
            return ;
        }
        if(me.try === 0) {
            $("#try").css("display", "block");
            return ;
        }
        me.try--;
        console.log("重试...");
        while(me.failed.length !== 0) {
            me.uploadPatch(parent, file, me.failed.pop(), shardCount);
        }
    },

    /**
     * 读取进度条（见笑了）
     * @param process
     */
    loadProcess: function (process) {
        process = Math.min(100, process);
        if(process === 100) {
            $("#try").css("display", "none");
        }
        $("#process").html(process.toFixed(2) + '<span>%</span>');
    },

    /**
     * 获取文件的 md5 值
     * @param file
     * @returns {*|number}
     */
    md5checkUpload: function (file) {
        var me = this;
        var index = 0;
        var shardCount = Math.ceil(file.size / this.shardSize);
        var spark = new SparkMD5.ArrayBuffer();
        var fileReader = new FileReader();
        var blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice;

        fileReader.onload = function (e) {
            index++;
            spark.append(e.target.result);
            if(index < shardCount) {
                loadNext();
                return;
            }
            me.checkUpload(file, spark.end());
        };

        function loadNext() {
            var start = index * me.shardSize;
            var end = Math.min(start + me.shardSize, file.size);
            fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
        }

        loadNext();
    }
};

$(function () {
    patchUpload.init();
});
