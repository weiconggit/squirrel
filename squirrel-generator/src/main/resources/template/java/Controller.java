package ${tablePackage}.${tableNameNoUnd};

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * <p>${tableNameCN} Cotroller</p>
 * @author ${author}
 * @time   ${strutil.formatDate(lastupdata,"yyyy-MM-dd")} 
 * @version ${version}
 */
@Api(tags={"${moduleName}-${tableNameCN}"})
@RestController
@RequestMapping(value = "${tableNameNoUnd}")
public class ${tableNameHump}Controller extends AbstractBaseController<${tableNameHump}Service, ${tableNameHump}VO>{
	
	
}
