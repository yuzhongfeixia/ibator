package org.apache.ibatis.ibator.generator.ibatis2.dao.elements;

import org.apache.ibatis.ibator.api.CommentGenerator;
import org.apache.ibatis.ibator.api.FullyQualifiedTable;
import org.apache.ibatis.ibator.api.IntrospectedTable;
import org.apache.ibatis.ibator.api.dom.java.Field;
import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.api.dom.java.InnerClass;
import org.apache.ibatis.ibator.api.dom.java.Interface;
import org.apache.ibatis.ibator.api.dom.java.JavaVisibility;
import org.apache.ibatis.ibator.api.dom.java.Method;
import org.apache.ibatis.ibator.api.dom.java.Parameter;
import org.apache.ibatis.ibator.api.dom.java.TopLevelClass;
import org.apache.ibatis.ibator.config.IbatorContext;

public class UpdateByExampleParmsInnerclassGenerator extends AbstractDAOElementGenerator
{
  public void addImplementationElements(TopLevelClass topLevelClass)
  {
    FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
    topLevelClass.addImportedType(this.introspectedTable.getExampleType());

    InnerClass innerClass = new InnerClass(
      new FullyQualifiedJavaType("UpdateByExampleParms"));
    innerClass.addDeprecatedAnnotation();

    innerClass.setVisibility(JavaVisibility.DEFAULT);
    innerClass.setStatic(true);
    innerClass.setSuperClass(this.introspectedTable.getExampleType());
    this.ibatorContext.getCommentGenerator().addClassComment(innerClass, table);

    Method method = new Method();
    method.addAnnotation("@Override");
    method.setConstructor(true);
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setName(innerClass.getType().getShortName());
    method.addParameter(
      new Parameter(FullyQualifiedJavaType.getObjectInstance(), 
      "record"));
    method.addParameter(
      new Parameter(this.introspectedTable.getExampleType(), 
      "example"));
    method.addBodyLine("super(example);");
    method.addBodyLine("this.record = record;");
    innerClass.addMethod(method);

    Field field = new Field();
    field.setVisibility(JavaVisibility.PRIVATE);
    field.setType(FullyQualifiedJavaType.getObjectInstance());
    field.setName("record");
    innerClass.addField(field);

    method = new Method();
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setReturnType(FullyQualifiedJavaType.getObjectInstance());
    method.setName("getRecord");
    method.addBodyLine("return record;");
    innerClass.addMethod(method);

    topLevelClass.addInnerClass(innerClass);
  }

  public void addInterfaceElements(Interface interfaze)
  {
  }
}