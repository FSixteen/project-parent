package com.grandland.janusgraph.work;

import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.schema.ConsistencyModifier;

import com.grandland.janusgraph.annotation.EdgeLabelEnum;
import com.grandland.janusgraph.annotation.IndexEnum;
import com.grandland.janusgraph.annotation.IndexEnum.Index;
import com.grandland.janusgraph.annotation.IndexEnum.Mapping;
import com.grandland.janusgraph.annotation.PropertyKeyEnum;
import com.grandland.janusgraph.annotation.VertexLabelEnum;

public class SchemaContent {

  @PropertyKeyEnum
  @IndexEnum(name = "name", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "name" }, mixedIndex = true, mixedIndexName = "search")
  private String name;
  @PropertyKeyEnum
  @IndexEnum(name = "uid", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "uid" }, mixedIndex = true, mixedIndexName = "search")
  private String uid;
  @PropertyKeyEnum
  @IndexEnum(name = "reg_person", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "reg_person" }, mixedIndex = true, mixedIndexName = "search")
  private String reg_person;
  @PropertyKeyEnum
  @IndexEnum(name = "reg_person_id", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "reg_person_id" }, mixedIndex = true, mixedIndexName = "search")
  private String reg_person_id;
  @PropertyKeyEnum
  @IndexEnum(name = "type", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "type" }, mixedIndex = true, mixedIndexName = "search")
  private String type;
  @PropertyKeyEnum
  @IndexEnum(name = "state", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "state" }, mixedIndex = true, mixedIndexName = "search")
  private String state;
  @PropertyKeyEnum
  @IndexEnum(name = "establish_time", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "establish_time" }, mixedIndex = true, mixedIndexName = "search")
  private String establish_time;
  @PropertyKeyEnum
  @IndexEnum(name = "reg_capital", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "reg_capital" }, mixedIndex = true, mixedIndexName = "search")
  private String reg_capital;
  @PropertyKeyEnum
  @IndexEnum(name = "scope", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "scope" }, mixedIndex = true, mixedIndexName = "search")
  private String scope;
  @PropertyKeyEnum
  @IndexEnum(name = "internal_num", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "internal_num" }, mixedIndex = true, mixedIndexName = "search")
  private String internal_num;
  @PropertyKeyEnum
  @IndexEnum(name = "reg_org_name", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "reg_org_name" }, mixedIndex = true, mixedIndexName = "search")
  private String reg_org_name;
  @PropertyKeyEnum
  @IndexEnum(name = "person_name", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "person_name" }, mixedIndex = true, mixedIndexName = "search")
  private String person_name;
  @PropertyKeyEnum
  @IndexEnum(name = "duty", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "duty" }, mixedIndex = true, mixedIndexName = "search")
  private String duty;
  @PropertyKeyEnum
  @IndexEnum(name = "person_id", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "person_id" }, mixedIndex = true, mixedIndexName = "search")
  private String person_id;
  @PropertyKeyEnum
  @IndexEnum(name = "subscribed_amount", index = { Index.Vertex }, mapping = Mapping.NULL, indexList = {
      "subscribed_amount" }, compositeIndex = true, consistencyModifier = ConsistencyModifier.DEFAULT)
  private Long subscribed_amount;
  @PropertyKeyEnum
  @IndexEnum(name = "percent", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = { "percent" }, mixedIndex = true, mixedIndexName = "search")
  private String percent;
  @PropertyKeyEnum
  @IndexEnum(name = "real_amount", index = { Index.Vertex }, mapping = Mapping.NULL, indexList = { "real_amount" }, compositeIndex = true, consistencyModifier = ConsistencyModifier.DEFAULT)
  private Long real_amount;

  // ----------------------------------------

  @VertexLabelEnum
  public String Person;

  @VertexLabelEnum
  public String Company;

  @VertexLabelEnum
  public String Department;

  @VertexLabelEnum
  public String ORG;

  // ----------------------------------------

  @EdgeLabelEnum(name = "INVEST_H")
  public String INVEST_H;

  @EdgeLabelEnum(multiplicity = Multiplicity.MULTI)
  public String INVEST_O;

  @EdgeLabelEnum(name = "KINSHIP", multiplicity = Multiplicity.MULTI)
  public String KINSHIP;

  @EdgeLabelEnum(name = "OWN", multiplicity = Multiplicity.MULTI, signature = "")
  public String OWN;

  @EdgeLabelEnum(name = "PAYMENT", multiplicity = Multiplicity.MULTI)
  public String PAYMENT;

  @EdgeLabelEnum(name = "Project", multiplicity = Multiplicity.MULTI)
  public String Project;

  @EdgeLabelEnum(name = "SERVE", multiplicity = Multiplicity.MULTI)
  public String SERVE;
}
