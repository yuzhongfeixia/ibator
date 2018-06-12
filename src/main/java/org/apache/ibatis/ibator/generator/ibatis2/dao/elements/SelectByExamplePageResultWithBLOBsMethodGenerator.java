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
import org.apache.ibatis.ibator.internal.util.messages.Messages;

public class SelectByExamplePageResultWithBLOBsMethodGenerator extends AbstractDAOElementGenerator {
	private boolean generateForJava5;

	public SelectByExamplePageResultWithBLOBsMethodGenerator(boolean generateForJava5) {
		this.generateForJava5 = generateForJava5;
	}

	public void addImplementationElements(TopLevelClass topLevelClass) {
		Set importedTypes = new TreeSet();
		importedTypes.add(this.ibatorContext.getTurnPageType());
		Method method = getMethodShell(importedTypes);
		method.addAnnotation("@Override");
		
		FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();

		StringBuilder sb = new StringBuilder();

		FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
		if (this.generateForJava5) {
			FullyQualifiedJavaType fqjt;
			if (this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
				fqjt = this.introspectedTable.getRecordWithBLOBsType();
			} else {
				fqjt = this.introspectedTable.getBaseRecordType();
			}

			importedTypes.add(fqjt);
			listType.addTypeArgument(fqjt);
		}

		if (this.ibatorContext.getTurnPageType() == null) {
			sb.append(listType.getShortName());
			sb.append(" list = ");

			String queryForListMethod = this.daoTemplate.getQueryForListMethod(table.getSqlMapNamespace(),
					XmlConstants.SELECT_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID, "example");
			int index = queryForListMethod.lastIndexOf(")");
			queryForListMethod = queryForListMethod.substring(0, index) + ", start, limit"
					+ queryForListMethod.substring(index);
			sb.append(queryForListMethod);

			method.addBodyLine(sb.toString());

			sb.setLength(0);
			sb.append(FullyQualifiedJavaType.getFullIntegerInstance().getShortName());
			sb.append(" totalSize = (");
			sb.append(FullyQualifiedJavaType.getFullIntegerInstance().getShortName());
			sb.append(")");
			sb.append(this.daoTemplate.getQueryForObjectMethod(table.getSqlMapNamespace(),
					XmlConstants.COUNT_BY_EXAMPLE_STATEMENT_ID, "example"));
			method.addBodyLine(sb.toString());

			sb.setLength(0);
			sb.append(method.getReturnType().getShortName());
			sb.append(" page = new ");
			sb.append(method.getReturnType().getShortName());
			sb.append("(start, limit, totalSize, list);");
			method.addBodyLine(sb.toString());

			method.addBodyLine("return page;");
		} else {
			sb.append("return new ");

			FullyQualifiedJavaType rtType = this.ibatorContext.getTurnPageType();

			sb.append(rtType.getShortName());
			sb.append("(sqlMapClient).selectByExampleWidth");
			sb.append(this.ibatorContext.getPageResultType().getShortName());
			sb.append("(\"");
			sb.append(table.getSqlMapNamespace());
			sb.append(".");
			sb.append(XmlConstants.SELECT_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID);
			sb.append("\", \"");
			sb.append(table.getSqlMapNamespace());
			sb.append(".");
			sb.append(XmlConstants.COUNT_BY_EXAMPLE_STATEMENT_ID);
			sb.append("\", example, start, limit);");
			method.addBodyLine(sb.toString());
		}

		if (this.ibatorContext.getPlugins().daoSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass,
				this.introspectedTable)) {
			topLevelClass.addImportedTypes(importedTypes);
			topLevelClass.addMethod(method);
		}
	}

	public void addInterfaceElements(Interface interfaze) {
		if (getExampleMethodVisibility() == JavaVisibility.PUBLIC) {
			Set importedTypes = new TreeSet();
			Method method = getMethodShell(importedTypes);

			if (this.ibatorContext.getPlugins().daoSelectByExampleWithBLOBsMethodGenerated(method, interfaze,
					this.introspectedTable)) {
				interfaze.addImportedTypes(importedTypes);
				interfaze.addMethod(method);
			}
		}
	}

	private Method getMethodShell(Set<FullyQualifiedJavaType> importedTypes) {
		FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
		FullyQualifiedJavaType type = this.introspectedTable.getExampleType();
		importedTypes.add(type);
		importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

		Method method = new Method();
		method.setVisibility(getExampleMethodVisibility());

		FullyQualifiedJavaType returnType = this.ibatorContext.getPageResultType();
		if (this.generateForJava5) {
			FullyQualifiedJavaType fqjt = getGenericTypes();
			importedTypes.add(fqjt);
			returnType.addTypeArgument(fqjt);
		}
		method.setReturnType(returnType);
		importedTypes.add(this.ibatorContext.getPageResultType());

		if (this.ibatorContext.getSuppressTypeWarnings(this.introspectedTable)) {
			method.addSuppressTypeWarningsAnnotation();
		}

		method.setName(getDAOMethodNameCalculator().getSelectByExampleWithBLOBsMethodName(this.introspectedTable));
		method.addParameter(new Parameter(type, "example"));

		method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "start"));
		method.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "limit"));

		for (FullyQualifiedJavaType fqjt : this.daoTemplate.getCheckedExceptions()) {
			method.addException(fqjt);
			importedTypes.add(fqjt);
		}

		this.ibatorContext.getCommentGenerator().addGeneralMethodComment(method, table);

		return method;
	}

	private FullyQualifiedJavaType getGenericTypes() {
		FullyQualifiedJavaType fqjt;
		if (this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
			fqjt = this.introspectedTable.getRecordWithBLOBsType();
		} else {
			if (this.introspectedTable.getRules().generateBaseRecordClass()) {
				fqjt = this.introspectedTable.getBaseRecordType();
			} else {
				if (this.introspectedTable.getRules().generatePrimaryKeyClass()) {
					fqjt = this.introspectedTable.getPrimaryKeyType();
				} else {
					throw new RuntimeException(Messages.getString("RuntimeError.12"));
				}
			}
		}
		return fqjt;
	}
}