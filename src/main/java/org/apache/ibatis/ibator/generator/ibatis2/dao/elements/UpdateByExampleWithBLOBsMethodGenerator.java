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

public class UpdateByExampleWithBLOBsMethodGenerator extends AbstractDAOElementGenerator {
	public void addImplementationElements(TopLevelClass topLevelClass) {
		Set importedTypes = new TreeSet();
		Method method = getMethodShell(importedTypes);
		method.addAnnotation("@Override");
		method.addDeprecatedAnnotation();
		FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();

		method.addBodyLine("UpdateByExampleParms parms = new UpdateByExampleParms(record, example);");

		StringBuilder sb = new StringBuilder();
		sb.append("int rows = ");
		sb.append(this.daoTemplate.getUpdateMethod(table.getSqlMapNamespace(),
				XmlConstants.UPDATE_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID, "parms"));
		method.addBodyLine(sb.toString());

		method.addBodyLine("return rows;");

		if (this.ibatorContext.getPlugins().daoUpdateByExampleWithBLOBsMethodGenerated(method, topLevelClass,
				this.introspectedTable)) {
			topLevelClass.addImportedTypes(importedTypes);
			topLevelClass.addMethod(method);
		}
	}

	public void addInterfaceElements(Interface interfaze) {
		if (getExampleMethodVisibility() == JavaVisibility.PUBLIC) {
			Set importedTypes = new TreeSet();
			Method method = getMethodShell(importedTypes);
			method.addDeprecatedAnnotation();

			if (this.ibatorContext.getPlugins().daoUpdateByExampleWithBLOBsMethodGenerated(method, interfaze,
					this.introspectedTable)) {
				interfaze.addImportedTypes(importedTypes);
				interfaze.addMethod(method);
			}
		}
	}

	private Method getMethodShell(Set<FullyQualifiedJavaType> importedTypes) {
		FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
		FullyQualifiedJavaType parameterType;
		if (this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
			parameterType = this.introspectedTable.getRecordWithBLOBsType();
		} else {
			if (this.introspectedTable.getRules().generateBaseRecordClass()) {
				parameterType = this.introspectedTable.getBaseRecordType();
			} else {
				parameterType = this.introspectedTable.getPrimaryKeyType();
			}
		}
		importedTypes.add(parameterType);

		Method method = new Method();
		method.setVisibility(getExampleMethodVisibility());
		method.setReturnType(FullyQualifiedJavaType.getIntInstance());
		method.setName(getDAOMethodNameCalculator().getUpdateByExampleWithBLOBsMethodName(this.introspectedTable));
		method.addParameter(new Parameter(parameterType, "record"));
		method.addParameter(new Parameter(this.introspectedTable.getExampleType(), "example"));

		for (FullyQualifiedJavaType fqjt : this.daoTemplate.getCheckedExceptions()) {
			method.addException(fqjt);
			importedTypes.add(fqjt);
		}

		this.ibatorContext.getCommentGenerator().addGeneralMethodComment(method, table);

		return method;
	}
}