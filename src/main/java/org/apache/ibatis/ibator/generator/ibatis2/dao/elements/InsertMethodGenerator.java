package org.apache.ibatis.ibator.generator.ibatis2.dao.elements;

import java.util.Set;
import java.util.TreeSet;
import org.apache.ibatis.ibator.api.CommentGenerator;
import org.apache.ibatis.ibator.api.DAOMethodNameCalculator;
import org.apache.ibatis.ibator.api.FullyQualifiedTable;
import org.apache.ibatis.ibator.api.IbatorPlugin;
import org.apache.ibatis.ibator.api.IntrospectedColumn;
import org.apache.ibatis.ibator.api.IntrospectedTable;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.Interface;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.Parameter;
import org.apache.ibatis.ibator.api.dom.java.PrimitiveTypeWrapper;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.config.GeneratedKey;
import org.apache.ibatis.ibator.config.IbatorContext;
import org.apache.ibatis.ibator.generator.ibatis2.XmlConstants;
import org.apache.ibatis.ibator.generator.ibatis2.dao.templates.AbstractDAOTemplate;
import org.apache.ibatis.ibator.internal.rules.IbatorRules;

public class InsertMethodGenerator extends AbstractDAOElementGenerator {
	public void addImplementationElements(TopLevelClass topLevelClass) {
		Set importedTypes = new TreeSet();
		Method method = getMethodShell(importedTypes);
		method.addAnnotation("@Override");

		method.addDeprecatedAnnotation();

		FullyQualifiedJavaType returnType = method.getReturnType();
		FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();

		StringBuilder sb = new StringBuilder();

		if (returnType != null) {
			sb.append("Object newKey = ");
		}

		sb.append(this.daoTemplate.getInsertMethod(table.getSqlMapNamespace(), XmlConstants.INSERT_STATEMENT_ID,
				"record"));
		method.addBodyLine(sb.toString());

		if (returnType != null) {
			if ("Object".equals(returnType.getShortName())) {
				method.addBodyLine("return newKey;");
			} else {
				sb.setLength(0);

				if (returnType.isPrimitive()) {
					PrimitiveTypeWrapper ptw = returnType.getPrimitiveTypeWrapper();
					sb.append("return ((");
					sb.append(ptw.getShortName());
					sb.append(") newKey");
					sb.append(").");
					sb.append(ptw.getToPrimitiveMethod());
					sb.append(';');
				} else {
					sb.append("return (");
					sb.append(returnType.getShortName());
					sb.append(") newKey;");
				}

				method.addBodyLine(sb.toString());
			}
		}

		if (this.ibatorContext.getPlugins().daoInsertMethodGenerated(method, topLevelClass, this.introspectedTable)) {
			topLevelClass.addImportedTypes(importedTypes);
			topLevelClass.addMethod(method);
		}
	}

	public void addInterfaceElements(Interface interfaze) {
		Set importedTypes = new TreeSet();
		Method method = getMethodShell(importedTypes);
		method.addDeprecatedAnnotation();

		if (this.ibatorContext.getPlugins().daoInsertMethodGenerated(method, interfaze, this.introspectedTable)) {
			interfaze.addImportedTypes(importedTypes);
			interfaze.addMethod(method);
		}
	}

	private Method getMethodShell(Set<FullyQualifiedJavaType> importedTypes) {
		FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
		Method method = new Method();
		FullyQualifiedJavaType returnType;
		if (this.introspectedTable.getGeneratedKey() != null) {
			IntrospectedColumn introspectedColumn = this.introspectedTable
					.getColumn(this.introspectedTable.getGeneratedKey().getColumn());
			if (introspectedColumn == null) {
				returnType = null;
			} else {
				returnType = introspectedColumn.getFullyQualifiedJavaType();
				importedTypes.add(returnType);
			}
		} else {
			returnType = null;
		}

		method.setReturnType(returnType);
		method.setVisibility(JavaVisibility.PUBLIC);
		DAOMethodNameCalculator methodNameCalculator = getDAOMethodNameCalculator();
		method.setName(methodNameCalculator.getInsertMethodName(this.introspectedTable));

		FullyQualifiedJavaType parameterType = this.introspectedTable.getRules().calculateAllFieldsClass();

		importedTypes.add(parameterType);
		method.addParameter(new Parameter(parameterType, "record"));

		for (FullyQualifiedJavaType fqjt : this.daoTemplate.getCheckedExceptions()) {
			method.addException(fqjt);
			importedTypes.add(fqjt);
		}

		this.ibatorContext.getCommentGenerator().addGeneralMethodComment(method, table);

		return method;
	}
}