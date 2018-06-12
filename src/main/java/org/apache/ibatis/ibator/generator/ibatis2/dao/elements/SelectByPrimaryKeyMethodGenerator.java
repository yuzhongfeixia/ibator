package org.apache.ibatis.ibator.generator.ibatis2.dao.elements;

import java.util.List;
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
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.config.IbatorContext;
import org.apache.ibatis.ibator.generator.ibatis2.XmlConstants;
import org.apache.ibatis.ibator.generator.ibatis2.dao.templates.AbstractDAOTemplate;
import org.apache.ibatis.ibator.internal.rules.IbatorRules;

public class SelectByPrimaryKeyMethodGenerator extends AbstractDAOElementGenerator
{
  public void addImplementationElements(TopLevelClass topLevelClass)
  {
    Set importedTypes = new TreeSet();
    Method method = getMethodShell(importedTypes);
    method.addAnnotation("@Override");
    
    StringBuilder sb = new StringBuilder();

    FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
    FullyQualifiedJavaType returnType = method.getReturnType();

    sb.append("return (");
    sb.append(returnType.getShortName());
    sb.append(")");
    sb.append(this.daoTemplate.getQueryForObjectMethod(table
      .getSqlMapNamespace(), 
      XmlConstants.SELECT_BY_PRIMARY_KEY_STATEMENT_ID, 
      this.introspectedTable.getRules().generatePrimaryKeyClass() ? "key" : ((IntrospectedColumn)this.introspectedTable.getPrimaryKeyColumns().get(0)).getJavaProperty()));

    method.addBodyLine(sb.toString());

    if (this.ibatorContext.getPlugins().daoSelectByPrimaryKeyMethodGenerated(method, topLevelClass, this.introspectedTable)) {
      topLevelClass.addImportedTypes(importedTypes);
      topLevelClass.addMethod(method);
    }
  }

  public void addInterfaceElements(Interface interfaze)
  {
    Set importedTypes = new TreeSet();
    Method method = getMethodShell(importedTypes);

    if (this.ibatorContext.getPlugins().daoSelectByPrimaryKeyMethodGenerated(method, interfaze, this.introspectedTable)) {
      interfaze.addImportedTypes(importedTypes);
      interfaze.addMethod(method);
    }
  }

  private Method getMethodShell(Set<FullyQualifiedJavaType> importedTypes) {
    FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();

    Method method = new Method();
    method.setVisibility(JavaVisibility.PUBLIC);

    FullyQualifiedJavaType returnType = this.introspectedTable.getRules()
      .calculateAllFieldsClass();
    method.setReturnType(returnType);
    importedTypes.add(returnType);

    method.setName(getDAOMethodNameCalculator()
      .getSelectByPrimaryKeyMethodName(this.introspectedTable));

    if (this.introspectedTable.getRules().generatePrimaryKeyClass()) {
      FullyQualifiedJavaType type = this.introspectedTable.getPrimaryKeyType();
      importedTypes.add(type);
      method.addParameter(new Parameter(type, "key"));
    } else {
      for (IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
        FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
        importedTypes.add(type);
        method.addParameter(new Parameter(type, introspectedColumn.getJavaProperty()));
      }
    }

    for (FullyQualifiedJavaType fqjt : this.daoTemplate.getCheckedExceptions()) {
      method.addException(fqjt);
      importedTypes.add(fqjt);
    }

    this.ibatorContext.getCommentGenerator().addGeneralMethodComment(method, 
      table);

    return method;
  }
}