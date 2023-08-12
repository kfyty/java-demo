<%@ page pageEncoding="utf-8" import="java.util.Date,java.text.SimpleDateFormat" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());%>
package com.kfyty.meta.space.model.entity;

import com.kfyty.meta.space.model.entity.base.AbstractDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 描述: ${table}: ${note}
 *
 * @author ${author}
 * @date <%=now%>
 * @email ${email}
 */
@Data
@Table("${table}")
@EqualsAndHashCode(callSuper = true)
public class ${className}DO extends AbstractDO {
<c:forEach var="field" items="${fields}" varStatus="status">
    <c:if test="${field.field != 'createTime' && field.field != 'updateTime'}">
    /**
     * ${columns[status.index].field}: ${field.fieldComment}
     */
    private ${field.fieldType} ${field.field};
    </c:if>
</c:forEach>
}
