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
  @IndexEnum(name = "uid", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "uid" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String uid;

  @PropertyKeyEnum
  @IndexEnum(name = "uid_short", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "uid_short" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String uid_short;

  @PropertyKeyEnum
  @IndexEnum(name = "name", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "name" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String name;

  @PropertyKeyEnum
  @IndexEnum(name = "birthday", index = { Index.Vertex }, mapping = Mapping.STRING, indexList = {
      "birthday" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String birthday;
  @PropertyKeyEnum
  @IndexEnum(name = "address", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "address" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String address;

  @PropertyKeyEnum
  @IndexEnum(name = "sex", index = { Index.Vertex }, mapping = Mapping.STRING, indexList = {
      "sex" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String sex;

  @PropertyKeyEnum
  @IndexEnum(name = "phone", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "phone" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String phone;

  @PropertyKeyEnum
  @IndexEnum(name = "education", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = {
      "education" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String education;

  @PropertyKeyEnum
  @IndexEnum(name = "cardno", index = { Index.Vertex }, mapping = Mapping.TEXTSTRING, indexList = {
      "cardno" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String cardno;

  @PropertyKeyEnum
  @IndexEnum(name = "ptype", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "ptype" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String ptype;

  /** 法人姓名 */
  @PropertyKeyEnum
  @IndexEnum(name = "reg_person", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "reg_person" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String reg_person;

  @PropertyKeyEnum
  @IndexEnum(name = "money", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "money" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double money;

  @PropertyKeyEnum
  @IndexEnum(name = "time", index = { Index.Vertex, Index.Edge }, mapping = Mapping.STRING, indexList = {
      "time" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String time;

  @PropertyKeyEnum
  @IndexEnum(name = "timestamp", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "timestamp" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Long timestamp;

  @PropertyKeyEnum
  @IndexEnum(name = "state", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "state" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String state;

  @PropertyKeyEnum
  @IndexEnum(name = "scope", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "scope" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String scope;

  @PropertyKeyEnum
  @IndexEnum(name = "tag", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "tag" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Long tag;

  @PropertyKeyEnum
  @IndexEnum(name = "ctype", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "ctype" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String ctype;

  @PropertyKeyEnum
  @IndexEnum(name = "regioncode", index = { Index.Vertex, Index.Edge }, mapping = Mapping.STRING, indexList = {
      "regioncode" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String regioncode;

  @PropertyKeyEnum
  @IndexEnum(name = "regionname", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "regionname" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String regionname;

  @PropertyKeyEnum
  @IndexEnum(name = "dtype", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "dtype" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String dtype;

  @PropertyKeyEnum
  @IndexEnum(name = "tips", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "tips" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String tips;

  @PropertyKeyEnum
  @IndexEnum(name = "real_amount", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "real_amount" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double real_amount;

  @PropertyKeyEnum
  @IndexEnum(name = "role", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "role" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String role;

  @PropertyKeyEnum
  @IndexEnum(name = "subscribed_amount", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "subscribed_amount" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double subscribed_amount;

  @PropertyKeyEnum
  @IndexEnum(name = "percent", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "percent" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double percent;

  @PropertyKeyEnum
  @IndexEnum(name = "status", index = { Index.Vertex, Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "status" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String status;

  @PropertyKeyEnum
  @IndexEnum(name = "registname", index = { Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "registname" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String registname;

  @PropertyKeyEnum
  @IndexEnum(name = "registid", index = { Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "registid" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String registid;

  @PropertyKeyEnum
  @IndexEnum(name = "duty", index = { Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "duty" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String duty;

  @PropertyKeyEnum
  @IndexEnum(name = "membership", index = { Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "membership" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String membership;

  @PropertyKeyEnum
  @IndexEnum(name = "category", index = { Index.Edge }, mapping = Mapping.TEXTSTRING, indexList = {
      "category" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String category;

  @PropertyKeyEnum
  @IndexEnum(name = "jointime", index = { Index.Edge }, mapping = Mapping.STRING, indexList = {
      "jointime" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String jointime;

  @PropertyKeyEnum
  @IndexEnum(name = "jointimestamp", index = { Index.Edge }, mapping = Mapping.NULL, indexList = {
      "jointimestamp" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Long jointimestamp;

  @PropertyKeyEnum
  @IndexEnum(name = "type", index = { Index.Vertex, Index.Edge }, mapping = Mapping.STRING, indexList = {
      "type" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private String type;

  @PropertyKeyEnum
  @IndexEnum(name = "updatetime", index = { Index.Vertex, Index.Edge }, mapping = Mapping.NULL, indexList = {
      "updatetime" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Long updatetime;

  @PropertyKeyEnum
  @IndexEnum(name = "neo4jid", index = { Index.Vertex }, mapping = Mapping.NULL, indexList = {
      "neo4jid" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Long neo4jid;

  @PropertyKeyEnum
  @IndexEnum(name = "fvid", index = { Index.Edge }, mapping = Mapping.NULL, indexList = {
      "fvid" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Long fvid;

  @PropertyKeyEnum
  @IndexEnum(name = "tvid", index = { Index.Edge }, mapping = Mapping.NULL, indexList = {
      "tvid" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Long tvid;
  
  @PropertyKeyEnum
  @IndexEnum(name = "pageRank", index = { Index.Vertex }, mapping = Mapping.NULL, indexList = {
      "pageRank" }, compositeIndex = false, consistencyModifier = ConsistencyModifier.DEFAULT, unique = false, mixedIndex = true, mixedIndexName = "search")
  private Double pageRank;

  // ----------------------------------------

  @VertexLabelEnum
  public String Person;

  @VertexLabelEnum
  public String Company;

  @VertexLabelEnum
  public String Department;

  @VertexLabelEnum
  private String Office;

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
