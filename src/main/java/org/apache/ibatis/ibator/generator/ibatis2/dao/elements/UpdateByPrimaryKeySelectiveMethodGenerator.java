package org.apache.ibatis.ibator.generator.ibatis2.dao.elements;

import java.util.Set;
import java.util.TreeSet;
import org.apache.ibatis.ibator.api.CommentGenerator;
import org.apache.ibatis.ibator.api.DAOMethodNameCalculator;
import org.apache.ibatis.ibator.api.FullyQualifiedTable;
import org.apache.ibatis.ibator.api.IbatorPlugin;
import org.apache.ibatis.ibator.api.IntrospectedTable;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.Interface;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.Parameter;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.config.IbatorContext;
import org.apache.ibatis.ibator.generator.ibatis2.XmlConstants;
import org.apache.ibatis.ibator.generator.ibatis2.dao.templates.AbstractDAOTemplate;
import org.apache.ibatis.ibator.internal.rules.IbatorRules;

public class UpdateByPrimaryKeySelectiveMethodGenerator extends AbstractDAOElementGenerator {
	public void addImplementationElements(TopLevelClass topLevelClass) {
		Set importedTypes = new TreeSet();
		Method method = getMethodShell(importedTypes);
		method.addAnnotation("@Override");
		
		FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();

		StringBuilder sb = new StringBuilder();

		sb.append("return ");
		sb.append(this.daoTemplate.getUpdateMethod(table.getSqlMapNamespace(),
				XmlConstants.UPDATE_BY_PRIMARY_KEY_SELECTIVE_STATEMENT_ID, "record"));
		method.addBodyLine(sb.toString());

		if (this.ibatorContext.getPlugins().daoUpdateByPrimaryKeySelectiveMethodGenerated(method, topLevelClass,
				this.introspectedTable)) {
			topLevelClass.addImportedTypes(importedTypes);
			topLevelClass.addMethod(method);
		}
	}

	public void addInterfaceElements(Interface interfaze) {
		Set importedTypes = new TreeSet();
		Method method = getMethodShell(importedTypes);

		if (this.ibatorContext.getPlugins().daoUpdateByPrimaryKeySelectiveMethodGenerated(method, interfaze,
				this.introspectedTable)) {
			interfaze.addImportedTypes(importedTypes);
			interfaze.addMethod(method);
		}
	}

	private Method getMethodShell(Set<FullyQualifiedJavaType> importedTypes) {
		FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
		FullyQualifiedJavaType parameterType;
		if (this.ibatorContext.isAddSetPropertyEnable())
			parameterType = this.introspectedTable.getRules().calculateAllSelectiveFieldsClass();
		else
			parameterType = this.introspectedTable.getRules().calculateAllFieldsClass();

		importedTypes.add(parameterType);

		Method method = new Method();
		method.setVisibility(JavaVisibility.PUBLIC);
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setName(getDAOMethodNameCalculator().getUpdateByPrimaryKeySelectiveMethodName(this.introspectedTable));
		method.addParameter(new Parameter(parameterType, "record"));

		for (FullyQualifiedJavaType fqjt : this.daoTemplate.getCheckedExceptions()) {
			method.addException(fqjt);
			importedTypes.add(fqjt);
		}

		this.ibatorContext.getCommentGenerator().addGeneralMethodComment(method, table);

		return method;
	}
}