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

public class SelectByExampleWithBLOBsMethodGenerator extends AbstractDAOElementGenerator
{
  private boolean generateForJava5;

  public SelectByExampleWithBLOBsMethodGenerator(boolean generateForJava5)
  {
    this.generateForJava5 = generateForJava5;
  }

  public void addImplementationElements(TopLevelClass topLevelClass)
  {
    Set importedTypes = new TreeSet();
    Method method = getMethodShell(importedTypes);
    method.addAnnotation("@Override");
    
    FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();

    if (this.generateForJava5) {
      method.addSuppressTypeWarningsAnnotation();
    }

    StringBuilder sb = new StringBuilder();

    sb.append("return ");
    sb.append(this.daoTemplate.getQueryForListMethod(table.getSqlMapNamespace(), 
      XmlConstants.SELECT_BY_EXAMPLE_WITH_BLOBS_STATEMENT_ID, 
      "example"));
    method.addBodyLine(sb.toString());

    if (this.ibatorContext.getPlugins().daoSelectByExampleWithBLOBsMethodGenerated(method, topLevelClass, this.introspectedTable)) {
      topLevelClass.addImportedTypes(importedTypes);
      topLevelClass.addMethod(method);
    }
  }

  public void addInterfaceElements(Interface interfaze)
  {
    if (getExampleMethodVisibility() == JavaVisibility.PUBLIC) {
      Set importedTypes = new TreeSet();
      Method method = getMethodShell(importedTypes);

      if (this.ibatorContext.getPlugins().daoSelectByExampleWithBLOBsMethodGenerated(method, interfaze, this.introspectedTable)) {
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

    FullyQualifiedJavaType returnType = 
      FullyQualifiedJavaType.getNewListInstance();
    if (this.generateForJava5)
    {
      FullyQualifiedJavaType fqjt;
      if (this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
        fqjt = this.introspectedTable.getRecordWithBLOBsType();
      }
      else {
        fqjt = this.introspectedTable.getBaseRecordType();
      }

      importedTypes.add(fqjt);
      returnType.addTypeArgument(fqjt);
    }
    method.setReturnType(returnType);

    if (this.ibatorContext.getSuppressTypeWarnings(this.introspectedTable)) {
      method.addSuppressTypeWarningsAnnotation();
    }

    method.setName(getDAOMethodNameCalculator()
      .getSelectByExampleWithBLOBsMethodName(this.introspectedTable));
    method.addParameter(new Parameter(type, "example"));

    for (FullyQualifiedJavaType fqjt : this.daoTemplate.getCheckedExceptions()) {
      method.addException(fqjt);
      importedTypes.add(fqjt);
    }

    this.ibatorContext.getCommentGenerator().addGeneralMethodComment(method, 
      table);

    return method;
  }
}