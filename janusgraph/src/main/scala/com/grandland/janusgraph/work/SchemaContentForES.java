package com.grandland.janusgraph.work;

import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.schema.ConsistencyModifier;

import com.grandland.janusgraph.annotation.EdgeLabelEnum;
import com.grandland.janusgraph.annotation.IndexEnum;
import com.grandland.janusgraph.annotation.IndexEnum.Index;
import com.grandland.janusgraph.annotation.IndexEnum.Mapping;
import com.grandland.janusgraph.annotation.PropertyKeyEnum;
import com.grandland.janusgraph.annotation.VertexLabelEnum;

public class SchemaContentForES {

  @PropertyKeyEnum
  @IndexEnum(name = "name", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "name" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String name;
  @PropertyKeyEnum
  @IndexEnum(name = "uid", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "uid" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String uid;
  @PropertyKeyEnum
  @IndexEnum(name = "reg_person", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "reg_person" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String reg_person;
  @PropertyKeyEnum
  @IndexEnum(name = "reg_person_id", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "reg_person_id" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String reg_person_id;
  @PropertyKeyEnum
  @IndexEnum(name = "type", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXT, indexList = {
      "type" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String type;

  @PropertyKeyEnum
  @IndexEnum(name = "ptype", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "ptype" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String ptype;
  @PropertyKeyEnum
  @IndexEnum(name = "ctype", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "ctype" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String ctype;
  @PropertyKeyEnum
  @IndexEnum(name = "dtype", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "dtype" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String dtype;

  @PropertyKeyEnum
  @IndexEnum(name = "state", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "state" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String state;
  @PropertyKeyEnum
  @IndexEnum(name = "establish_time", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "establish_time" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String establish_time;
  @PropertyKeyEnum
  @IndexEnum(name = "reg_capital", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "reg_capital" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String reg_capital;
  @PropertyKeyEnum
  @IndexEnum(name = "scope", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "scope" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String scope;
  @PropertyKeyEnum
  @IndexEnum(name = "internal_num", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "internal_num" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String internal_num;
  @PropertyKeyEnum
  @IndexEnum(name = "reg_org_name", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "reg_org_name" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String reg_org_name;
  @PropertyKeyEnum
  @IndexEnum(name = "person_name", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "person_name" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String person_name;
  @PropertyKeyEnum
  @IndexEnum(name = "duty", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "duty" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String duty;
  @PropertyKeyEnum
  @IndexEnum(name = "person_id", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "person_id" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String person_id;
  @PropertyKeyEnum
  @IndexEnum(name = "subscribed_amount", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "subscribed_amount" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double subscribed_amount;
  @PropertyKeyEnum
  @IndexEnum(name = "percent", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "percent" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double percent;
  @PropertyKeyEnum
  @IndexEnum(name = "real_amount", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "real_amount" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double real_amount;

  // ----------------------------------------

  @PropertyKeyEnum
  @IndexEnum(name = "bet", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "bet" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String bet;
  @PropertyKeyEnum
  @IndexEnum(name = "born", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "born" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String born;
  @PropertyKeyEnum
  @IndexEnum(name = "date", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "date" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String date;
  @PropertyKeyEnum
  @IndexEnum(name = "degree", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "degree" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Integer degree;
  @PropertyKeyEnum
  @IndexEnum(name = "invest", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "invest" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double invest;
  @PropertyKeyEnum
  @IndexEnum(name = "membership", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "membership" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String membership;
  @PropertyKeyEnum
  @IndexEnum(name = "money", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "money" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double money;
  @PropertyKeyEnum
  @IndexEnum(name = "money_invest", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "money_invest" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double money_invest;
  @PropertyKeyEnum
  @IndexEnum(name = "money_invest_h", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "money_invest_h" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double money_invest_h;
  @PropertyKeyEnum
  @IndexEnum(name = "order", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "order" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String order;
  @PropertyKeyEnum
  @IndexEnum(name = "pageRank", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "pageRank" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double pageRank;
  @PropertyKeyEnum
  @IndexEnum(name = "rating", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "rating" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String rating;
  @PropertyKeyEnum
  @IndexEnum(name = "reg_num", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "reg_num" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String reg_num;
  @PropertyKeyEnum
  @IndexEnum(name = "released", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "released" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String released;
  @PropertyKeyEnum
  @IndexEnum(name = "role", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "role" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String role;
  @PropertyKeyEnum
  @IndexEnum(name = "roles", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "roles" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String roles;
  @PropertyKeyEnum
  @IndexEnum(name = "summary", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "summary" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String summary;
  @PropertyKeyEnum
  @IndexEnum(name = "tagline", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "tagline" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String tagline;
  @PropertyKeyEnum
  @IndexEnum(name = "tax", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "tax" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double tax;
  @PropertyKeyEnum
  @IndexEnum(name = "time", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "time" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String time;
  @PropertyKeyEnum
  @IndexEnum(name = "time_stamp", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "time_stamp" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Long time_stamp;
  @PropertyKeyEnum
  @IndexEnum(name = "tips", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "tips" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String tips;
  @PropertyKeyEnum
  @IndexEnum(name = "title", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "title" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String title;
  @PropertyKeyEnum
  @IndexEnum(name = "value", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "value" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String value;
  @PropertyKeyEnum
  @IndexEnum(name = "targetID", index = { Index.Vertex }, mapping = Mapping.NULL, indexList = {
      "targetID" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Long targetID;

  @PropertyKeyEnum
  @IndexEnum(name = "updatetime", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "updatetime" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Long updatetime;

  // ----------------------------------------

  @VertexLabelEnum
  public String Person;

  @VertexLabelEnum
  public String Company;

  @VertexLabelEnum
  public String Department;

  @VertexLabelEnum
  public String ORG;

  @VertexLabelEnum
  public String Car;

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
