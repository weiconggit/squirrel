package ${tablePackage}.${tableNameNoUnd};

import org.weicong.generator.data.ColumnData;

import io.swagger.annotations.ApiModel;
import net.sf.jsqlparser.schema.Column;
import net.sf.oval.constraint.Length;
import net.sf.oval.constraint.Max;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.Range;

/**
 * <p>${tableNameCN} 实体 bean</p>
 * @author ${author}
 * @time   ${strutil.formatDate(lastupdata,"yyyy-MM-dd")} 
 * @version ${version}
 */
@ApiModel(value = "${tableNameCN}", subTypes = ${tableNameHump}VO.class)
public class ${tableNameHump} {

<% for(column in columnDatas) {%>
	@ApiModelProperty("${column.nameCN}")
	<%if (column.type == "String" && column.length > 0) {%>
		<%if (column.isNullable) {%> 
	@MaxLength(value = ${column.length})
		<%} else {%>
	@NotNull
	@NotEmpty
	@Length(min = 1, max = ${column.length})
		<%}%>
	<%}%>
	<%if (column.type == "Integer" && column.length > 0) {%>
		<%if (column.isNullable) {%>
	@Max(value = ${column.length})
		<%} else {%>
	@NotNull
	@Range(min = 1, max = ${column.length})
		<%}%>
	<%}%>
    private ${column.type} ${column.name};
    
<% }%>

<% for(column in columnDatas) {%>
	/**
	 * set ${column.nameCN}
	 * @return ${tableNameHump}
	 */
	public ${tableNameHump} set${column.nameHump}(${column.type} ${column.name}){
		this.${column.name} = ${column.name};
		return this;
	}
	/**
	 * get ${column.nameCN}
	 * @return ${tableNameHump}
	 */
	public ${column.type} get${column.nameHump}(){
		return this.${column.name};
	}
	
<% }%>
	@Override
	public String toString(){
		StringBuilder sb =new StringBuilder();
		sb.append("${tableNameHump}:[");
<%for(column in columnDatas) {%>
		sb.append(", ${column.name}="+this.get${column.nameHump}());
<%}%>
		sb.append("]");
		return sb.toString();
	}

}
