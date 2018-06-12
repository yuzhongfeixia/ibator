/*
 * Copyright 2005 The Apache Software Foundation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.ibatis.ibator.api;

import org.apache.ibatis.ibator.api.dom.java.FullyQualifiedJavaType;
import org.apache.ibatis.ibator.config.IbatorContext;
import org.apache.ibatis.ibator.internal.types.JdbcTypeNameTranslator;
import org.apache.ibatis.ibator.internal.util.StringUtility;

public class IntrospectedColumn
{
  protected String actualColumnName;
  protected int jdbcType;
  protected String jdbcTypeName;
  protected boolean nullable;
  protected int length;
  protected int scale;
  protected boolean identity;
  private String _javaProperty;
  protected String javaProperty;
  protected String isSetJavaProperty;
  protected String isPlusJavaProperty;
  protected String sqlSnippetJavaProperty;
  private boolean isNumberIncremental;
  private boolean isPrimaryKey;
  protected FullyQualifiedJavaType fullyQualifiedJavaType;
  protected String tableAlias;
  protected String typeHandler;
  protected IbatorContext ibatorContext;
  protected boolean isColumnNameDelimited;
  protected IntrospectedTable introspectedTable;
  
  public int getJdbcType()
  {
    return this.jdbcType;
  }
  
  public void setJdbcType(int jdbcType)
  {
    this.jdbcType = jdbcType;
  }
  
  public int getLength()
  {
    return this.length;
  }
  
  public void setLength(int length)
  {
    this.length = length;
  }
  
  public boolean isNullable()
  {
    return this.nullable;
  }
  
  public void setNullable(boolean nullable)
  {
    this.nullable = nullable;
  }
  
  public int getScale()
  {
    return this.scale;
  }
  
  public void setScale(int scale)
  {
    this.scale = scale;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    
    sb.append("Actual Column Name: ");
    sb.append(this.actualColumnName);
    sb.append(", JDBC Type: ");
    sb.append(this.jdbcType);
    sb.append(", Nullable: ");
    sb.append(this.nullable);
    sb.append(", Length: ");
    sb.append(this.length);
    sb.append(", Scale: ");
    sb.append(this.scale);
    sb.append(", Identity: ");
    sb.append(this.identity);
    
    return sb.toString();
  }
  
  public void setActualColumnName(String actualColumnName)
  {
    this.actualColumnName = actualColumnName;
    this.isColumnNameDelimited = StringUtility.stringContainsSpace(actualColumnName);
  }
  
  public boolean isIdentity()
  {
    return this.identity;
  }
  
  public void setIdentity(boolean identity)
  {
    this.identity = identity;
  }
  
  public boolean isNumberIncremental()
  {
    return this.isNumberIncremental;
  }
  
  public void setNumberIncremental(boolean isNumberIncremental)
  {
    this.isNumberIncremental = isNumberIncremental;
  }
  
  public boolean isPrimaryKey()
  {
    return this.isPrimaryKey;
  }
  
  public void setPrimaryKey(boolean isPrimaryKey)
  {
    this.isPrimaryKey = isPrimaryKey;
  }
  
  public boolean isBLOBColumn()
  {
    String typeName = getJdbcTypeName();
    return ("BINARY".equals(typeName)) || ("BLOB".equals(typeName)) || 
      ("CLOB".equals(typeName)) || ("LONGVARBINARY".equals(typeName)) || 
      ("LONGVARCHAR".equals(typeName)) || ("VARBINARY".equals(typeName));
  }
  
  public boolean isStringColumn()
  {
    return this.fullyQualifiedJavaType.equals(
      FullyQualifiedJavaType.getStringInstance());
  }
  
  public boolean isNumberColumn()
  {
    return (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getBaseByteInstance())) || 
      (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getFullByteInstance())) || 
      (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getBaseShortInstance())) || 
      (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getFullShortInstance())) || 
      (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getIntInstance())) || 
      (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getFullIntegerInstance())) || 
      (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getBaseLongInstance())) || 
      (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getFullLongInstance())) || 
      (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getBaseFloatInstance())) || 
      (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getFullFloatInstance())) || 
      (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getBaseDoubleInstance())) || 
      (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getFullDoubleInstance()));
  }
  
  public boolean isJdbcCharacterColumn()
  {
    return (this.jdbcType == 1) || 
      (this.jdbcType == 2005) || 
      (this.jdbcType == -1) || 
      (this.jdbcType == 12);
  }
  
  public String getJavaProperty(boolean firstCharUpper)
  {
    if (firstCharUpper) {
      return this._javaProperty;
    }
    return getJavaProperty();
  }
  
  public String getJavaProperty()
  {
    return getJavaProperty(null);
  }
  
  public String getJavaProperty(String prefix)
  {
    if (prefix == null) {
      return this.javaProperty;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(prefix);
    sb.append(this.javaProperty);
    
    return sb.toString();
  }
  
  public String getIsSetJavaProperty()
  {
    return getIsSetJavaProperty(null);
  }
  
  public String getIsSetJavaProperty(String prefix)
  {
    if (prefix == null) {
      return this.isSetJavaProperty;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(prefix);
    sb.append(this.isSetJavaProperty);
    return sb.toString();
  }
  
  public String getIsPlusJavaProperty()
  {
    return getIsPlusJavaProperty(null);
  }
  
  public String getIsPlusJavaProperty(String prefix)
  {
    if (prefix == null) {
      return this.isPlusJavaProperty;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(prefix);
    sb.append(this.isPlusJavaProperty);
    return sb.toString();
  }
  
  public void setJavaProperty(String javaProperty)
  {
    this.javaProperty = javaProperty;
    StringBuilder sb = new StringBuilder(javaProperty);
    sb.setCharAt(0, Character.toUpperCase(javaProperty.charAt(0)));
    this._javaProperty = sb.toString();
    


    this.isSetJavaProperty = (this.javaProperty + "$seted");
    this.sqlSnippetJavaProperty = (this.javaProperty + "$sqlSnippeted");
    this.isPlusJavaProperty = (this.javaProperty + "$plused");
  }
  
  public String getRenamedColumnNameForResultMap()
  {
    if (StringUtility.stringHasValue(this.tableAlias))
    {
      StringBuilder sb = new StringBuilder();
      
      sb.append(this.tableAlias);
      sb.append('_');
      sb.append(this.actualColumnName);
      return sb.toString();
    }
    return this.actualColumnName;
  }
  
  public String getSelectListPhrase()
  {
    if (StringUtility.stringHasValue(this.tableAlias))
    {
      StringBuilder sb = new StringBuilder();
      
      sb.append(getAliasedEscapedColumnName());
      sb.append(" as ");
      if (this.isColumnNameDelimited) {
        sb.append(this.ibatorContext.getBeginningDelimiter());
      }
      sb.append(this.tableAlias);
      sb.append('_');
      sb.append(StringUtility.escapeStringForIbatis(this.actualColumnName));
      if (this.isColumnNameDelimited) {
        sb.append(this.ibatorContext.getEndingDelimiter());
      }
      return sb.toString();
    }
    return getEscapedColumnName();
  }
  
  public boolean isJDBCDateColumn()
  {
    return (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getDateInstance())) && 
      ("DATE".equalsIgnoreCase(this.jdbcTypeName));
  }
  
  public boolean isJDBCTimeColumn()
  {
    return (this.fullyQualifiedJavaType.equals(FullyQualifiedJavaType.getDateInstance())) && 
      ("TIME".equalsIgnoreCase(this.jdbcTypeName));
  }
  
  public String getIbatisFormattedParameterClause()
  {
    return getIbatisFormattedParameterClause(null);
  }
  
  /**
   * 屏蔽生成update时增加的类型
   * update T_B_OPERATOR_WAREHOUSE
	 *   <dynamic prepend="set" >
	 *     <isNotNull prepend="," property="record.operatorId" >
	 *       OPERATOR_ID = #record.operatorId#
	 *     </isNotNull>
	 *     <isNotNull prepend="," property="record.warehouseId" >
	 *       WAREHOUSE_ID = #record.warehouseId#
	 *     </isNotNull>
	 *   </dynamic>
	 *
	 *	2018-03-29 modify by chengen
	 * 
   * @param prefix
   * @return
   */
  public String getIbatisFormattedParameterClause(String prefix)
  {
    StringBuilder sb = new StringBuilder();
    
    sb.append('#');
    sb.append(getJavaProperty(prefix));
    if (StringUtility.stringHasValue(this.typeHandler))
    {
      sb.append(",jdbcType=");
      sb.append(getJdbcTypeName());
      sb.append(",handler=");
      sb.append(this.typeHandler);
    }
//    else
//    {
//      sb.append(':');
//      sb.append(getJdbcTypeName());
//    }
    sb.append('#');
    
    return sb.toString();
  }
  
  public String getTypeHandler()
  {
    return this.typeHandler;
  }
  
  public void setTypeHandler(String typeHandler)
  {
    this.typeHandler = typeHandler;
  }
  
  public String getActualColumnName()
  {
    return this.actualColumnName;
  }
  
  public String getEscapedColumnName()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(StringUtility.escapeStringForIbatis(this.actualColumnName));
    if (this.isColumnNameDelimited)
    {
      sb.insert(0, this.ibatorContext.getBeginningDelimiter());
      sb.append(this.ibatorContext.getEndingDelimiter());
    }
    return sb.toString();
  }
  
  public String getAliasedActualColumnName()
  {
    StringBuilder sb = new StringBuilder();
    if (StringUtility.stringHasValue(this.tableAlias))
    {
      sb.append(this.tableAlias);
      sb.append('.');
    }
    if (this.isColumnNameDelimited) {
      sb.append(StringUtility.escapeStringForJava(this.ibatorContext.getBeginningDelimiter()));
    }
    sb.append(this.actualColumnName);
    if (this.isColumnNameDelimited) {
      sb.append(StringUtility.escapeStringForJava(this.ibatorContext.getEndingDelimiter()));
    }
    return sb.toString();
  }
  
  public String getAliasedEscapedColumnName()
  {
    if (StringUtility.stringHasValue(this.tableAlias))
    {
      StringBuilder sb = new StringBuilder();
      
      sb.append(this.tableAlias);
      sb.append('.');
      sb.append(getEscapedColumnName());
      return sb.toString();
    }
    return getEscapedColumnName();
  }
  
  public void setColumnNameDelimited(boolean isColumnNameDelimited)
  {
    this.isColumnNameDelimited = isColumnNameDelimited;
  }
  
  public boolean isColumnNameDelimited()
  {
    return this.isColumnNameDelimited;
  }
  
  public String getJdbcTypeName()
  {
    if (this.jdbcTypeName == null) {
      this.jdbcTypeName = JdbcTypeNameTranslator.getJdbcTypeName(this.jdbcType);
    }
    return this.jdbcTypeName;
  }
  
  public void setJdbcTypeName(String jdbcTypeName)
  {
    this.jdbcTypeName = jdbcTypeName;
  }
  
  public FullyQualifiedJavaType getFullyQualifiedJavaType()
  {
    return this.fullyQualifiedJavaType;
  }
  
  public void setFullyQualifiedJavaType(FullyQualifiedJavaType fullyQualifiedJavaType)
  {
    this.fullyQualifiedJavaType = fullyQualifiedJavaType;
  }
  
  public String getTableAlias()
  {
    return this.tableAlias;
  }
  
  public void setTableAlias(String tableAlias)
  {
    this.tableAlias = tableAlias;
  }
  
  public IbatorContext getIbatorContext()
  {
    return this.ibatorContext;
  }
  
  public void setIbatorContext(IbatorContext ibatorContext)
  {
    this.ibatorContext = ibatorContext;
  }
  
  public IntrospectedTable getIntrospectedTable()
  {
    return this.introspectedTable;
  }
  
  public void setIntrospectedTable(IntrospectedTable introspectedTable)
  {
    this.introspectedTable = introspectedTable;
  }
}

