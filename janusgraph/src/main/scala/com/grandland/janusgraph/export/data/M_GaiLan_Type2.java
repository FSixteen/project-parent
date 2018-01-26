package com.grandland.janusgraph.export.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.janusgraph.graphdb.relations.CacheEdge;
import org.janusgraph.graphdb.vertices.CacheVertex;

import com.google.bigtable.repackaged.com.google.gson.Gson;
import com.grandland.janusgraph.core.ESConnection;
import com.grandland.janusgraph.core.GraphFactory;
import com.grandland.janusgraph.core.LongEncoding;

/**
 * 概览大图, type=2
 * 
 * @author Shengjun Liu<br/>
 * @version 2018-01-17<br/>
 *
 */
public class M_GaiLan_Type2 {
  public static AtomicInteger size = new AtomicInteger(0);

  public static void main(String[] args) {
    GraphTraversalSource g = GraphFactory.getInstance().builderConfig().getG();
    ArrayList<Long> temp = new ArrayList<Long>();
    for (Integer page = 0;; page++) {
      /** 保存ES中获取的Department的ID信息 */
      List<Long> ids = new ArrayList<Long>();
      /** 从ES中获取的Department的ID信息,放入{es_id_s}中 */
      // esGet(ids, page);
      ids = Arrays.asList(163872952L, 81993904L, 40976472L, 163868856L, 3522572408L, 40980672L, 81989808L, 81985712L, 81977520L, 1474568328L, 81973424L, 81969328L, 163864760L, 1474564232L, 40972376L,
          3522568312L, 81948912L, 40972368L, 3481628920L, 40968272L, 81965232L, 81944816L, 40976576L, 81961136L, 3522564216L, 81948848L, 163860664L, 3481624824L, 81936624L, 163856568L, 81940656L,
          40972480L, 3481620728L, 40968384L, 81932528L, 81936560L, 40964288L, 3481616632L, 163852472L, 81928432L, 3481612536L, 81932464L, 81928368L, 3481608440L, 40964176L, 81924336L, 163844280L,
          81924272L, 3481604344L, 40964184L, 1358413976L, 462984L, 434240L, 401656L, 471208L, 925768L, 430144L, 458888L, 426048L, 397560L, 438320L, 446584L, 446600L, 442488L, 467112L, 430128L,
          434296L, 426032L, 417856L, 430200L, 446632L, 413840L, 405656L, 401472L, 401552L, 393360L, 397456L, 405672L, 413816L, 426120L, 843848L, 847944L, 381072L, 385088L, 839752L, 389272L, 360696L,
          380992L, 356600L, 409720L, 389168L, 831560L, 352504L, 381080L, 372880L, 385072L, 376984L, 372888L, 422024L, 823368L, 819272L, 380976L, 344312L, 376880L, 405624L, 368792L, 381096L, 417928L,
          811080L, 368784L, 413832L, 806984L, 376896L, 401528L, 397432L, 372904L, 409736L, 364688L, 401544L, 782408L, 393352L, 389256L, 385160L, 778312L, 356504L, 352408L, 332024L, 774216L, 352400L,
          356400L, 327928L, 352304L, 356488L, 348208L, 364608L, 757832L, 344216L, 340016L, 352424L, 331824L, 372856L, 348328L, 327728L, 340136L, 737352L, 360568L, 336040L, 356416L, 344208L, 331928L,
          352320L, 336016L, 720968L, 352376L, 331920L, 704584L, 700488L, 327832L, 327824L, 303352L, 303152L, 299056L, 323752L, 344184L, 319656L, 323736L, 340088L, 688200L, 319632L, 344200L, 290864L,
          299256L, 319640L, 675912L, 307368L, 335992L, 340032L, 295160L, 667720L, 274480L, 327800L, 323704L, 319608L, 315512L, 655432L, 286968L, 303256L, 335936L, 340104L, 266288L, 299160L, 647240L,
          336008L, 323648L, 639048L, 299128L, 634952L, 331912L, 630856L, 315536L, 295032L, 290936L, 2686001224L, 319552L, 626760L, 327816L, 315456L, 278776L, 311440L, 622664L, 618568L, 323720L,
          614472L, 286840L, 307344L, 303248L, 610376L, 274680L, 299152L, 307264L, 282744L, 258096L, 299176L, 602184L, 254000L, 299072L, 290984L, 598088L, 249904L, 274552L, 307336L, 593992L, 299144L,
          295048L, 585800L, 589896L, 270584L, 266488L, 290880L, 286872L, 581704L, 290952L, 286784L, 286856L, 282792L, 282688L, 282760L, 290960L, 577608L, 573512L, 569416L, 565320L, 561224L, 286864L,
          274584L, 557128L, 553032L, 245808L, 548936L, 278672L, 274576L, 241712L, 274600L, 266392L, 266360L, 278592L, 274496L, 254200L, 258296L, 266384L, 237616L, 233520L, 262296L, 262288L, 544840L,
          262264L, 270400L, 250104L, 278664L, 258200L, 540744L, 536648L, 254104L, 270504L, 532552L, 266408L, 528456L, 524360L, 241912L, 520264L, 516168L, 274568L, 512072L, 225328L, 507976L, 262312L,
          503880L, 266304L, 221232L, 499784L, 262208L, 258112L, 254016L, 258216L, 250008L, 254120L, 495688L, 491592L, 487496L, 245912L, 483400L, 249976L, 241816L, 270472L, 479304L, 245880L, 475208L,
          237816L, 266376L, 254096L, 467016L, 471112L, 217136L, 462920L, 245824L, 458824L, 262280L, 250000L, 237720L, 245904L, 258184L, 241728L, 241784L, 213040L, 233720L, 454728L, 241808L, 208944L,
          254088L, 237632L, 450632L, 446536L, 229624L, 442440L, 225528L, 237712L, 438344L, 233616L, 434248L, 249992L, 221432L, 204848L, 237688L, 426056L, 229520L, 233624L, 233592L, 217336L, 229496L,
          229528L, 225400L, 421960L, 200752L, 213240L, 241832L, 417864L, 233536L, 209144L, 237736L, 229440L, 225344L, 229544L, 221248L, 217152L, 225432L, 205048L, 221336L, 413768L, 217240L, 409672L,
          225424L, 196656L, 221304L, 225448L, 213056L, 192560L, 245896L, 405576L, 200952L, 196856L, 241800L, 401480L, 237704L, 213144L, 233608L, 213112L, 208960L, 397384L, 209048L, 204864L, 221352L,
          192760L, 229512L, 393288L, 389192L, 221328L, 217256L, 200768L, 184368L, 204952L, 385096L, 381000L, 209016L, 217232L, 196672L, 376904L, 213160L, 192576L, 209064L, 188664L, 204920L, 200824L,
          204968L, 225416L, 221320L, 200856L, 217224L, 372808L, 213128L, 196728L, 188480L, 209032L, 200872L, 192632L, 368712L, 213136L, 180472L, 364616L, 184384L, 196776L, 172280L, 176376L, 180288L,
          360520L, 192680L, 176176L, 172080L, 209040L, 356424L, 204944L, 176192L, 196760L, 167984L, 352328L, 196752L, 348232L, 344136L, 340040L, 204936L, 192664L, 188568L, 184472L, 188536L, 192656L,
          163888L, 335944L, 172096L, 184440L, 200840L, 188560L, 196744L, 180376L, 168000L, 331848L, 192648L, 176280L, 159792L, 188584L, 327752L, 184488L, 163904L, 188552L, 168184L, 323656L, 180344L,
          159808L, 319560L, 164088L, 184456L, 184464L, 155712L, 180368L, 155696L, 176272L, 172184L, 315464L, 180392L, 176248L, 151616L, 172152L, 168056L, 159992L, 311368L, 307272L, 303176L, 155896L,
          151600L, 168088L, 172176L, 168080L, 163984L, 163960L, 163992L, 151800L, 147520L, 143424L, 159864L, 147704L, 299080L, 176296L, 294984L, 155768L, 172200L, 290888L, 159896L, 143608L, 168104L,
          155800L, 176264L, 139512L, 147504L, 159888L, 139328L, 151672L, 135416L, 155792L, 286792L, 282696L, 135232L, 151696L, 143408L, 278600L, 172168L, 274504L, 131136L, 151704L, 164008L, 270408L,
          168072L, 147600L, 139312L, 147576L, 127040L, 159912L, 122944L, 147608L, 143480L, 266312L, 143504L, 135216L, 159880L, 118848L, 143512L, 139408L, 131320L, 155816L, 135312L, 139416L, 262216L,
          155784L, 135320L, 127224L, 151688L, 131216L, 258120L, 139384L, 127120L, 151720L, 114752L, 131120L, 135288L, 254024L, 119032L, 147624L, 114936L, 131224L, 110840L, 122928L, 110656L, 123024L,
          249928L, 118928L, 127128L, 114832L, 147592L, 143496L, 245832L, 241736L, 110736L, 106744L, 143528L, 237640L, 123032L, 106640L, 139432L, 118936L, 114840L, 102544L, 135336L, 131240L, 233544L,
          229448L, 131192L, 127096L, 123000L, 225352L, 118904L, 110744L, 106560L, 127144L, 123048L, 221256L, 217160L, 139400L, 135304L, 118952L, 131208L, 106648L, 127112L, 118832L, 213064L, 102464L,
          208968L, 102648L, 114856L, 98552L, 123016L, 118920L, 114824L, 110728L, 204872L, 114808L, 94360L, 110760L, 90360L, 114736L, 106664L, 90264L, 106632L, 110640L, 110712L, 98448L, 200776L,
          98368L, 102536L, 106544L, 94272L, 102448L, 196680L, 192584L, 98352L, 188488L, 184392L, 180296L, 94256L, 176200L, 102568L, 90160L, 98440L, 106616L, 86168L, 102520L, 94344L, 86064L, 98472L,
          94376L, 94352L, 90176L, 82072L, 90280L, 86184L, 98424L, 86264L, 78072L, 86080L, 73976L, 82088L, 90248L, 81984L, 172104L, 168008L, 69880L, 94328L, 77992L, 77976L, 159816L, 86152L, 90232L,
          90256L, 73880L, 69784L, 77888L, 73792L, 155720L, 86160L, 82056L, 65688L, 73896L, 151624L, 81968L, 147528L, 69800L, 69696L, 143432L, 65704L, 82064L, 139336L, 77968L, 77960L, 135240L, 73864L,
          77872L, 65600L, 131144L, 127048L, 73776L, 61592L, 122952L, 86136L, 69680L, 118856L, 82040L, 77944L, 61608L, 69768L, 65672L, 65784L, 57496L, 57512L, 73848L, 61576L, 69752L, 61504L, 65584L,
          57480L, 73872L, 114760L, 61488L, 53416L, 65656L, 49320L, 53400L, 110664L, 106568L, 102472L, 49304L, 98376L, 61560L, 57408L, 57464L, 53368L, 53384L, 69776L, 45208L, 49288L, 57392L, 41112L,
          37016L, 45224L, 65680L, 57592L, 32920L, 41128L, 53312L, 94280L, 53296L, 49272L, 90184L, 53496L, 37032L, 41096L, 45176L, 32936L, 86088L, 37000L, 41080L, 28824L, 36984L, 61584L, 49200L,
          32904L, 81992L, 77896L, 49400L, 49216L, 28840L, 45120L, 57488L, 45304L, 73800L, 69704L, 41024L, 24728L, 65608L, 20632L, 28808L, 45104L, 24744L, 53392L, 41208L, 36928L, 24712L, 49296L,
          32888L, 61512L, 32832L, 20616L, 57416L, 45200L, 28792L, 41008L, 53320L, 41104L, 16536L, 37112L, 33016L, 36912L, 28920L, 37008L, 49224L, 16520L, 45128L, 28736L, 24824L, 12440L, 24696L,
          32912L, 20648L, 28816L, 16552L, 41032L, 36936L, 32816L, 20728L, 12456L, 32840L, 28744L, 20600L, 24640L, 20544L, 8344L, 1347620984L, 16504L, 12408L, 4248L, 16448L, 28720L, 24648L, 24720L,
          8312L, 12352L, 20552L, 12424L, 16456L, 24624L, 16632L, 8328L, 12536L, 4216L, 20528L, 20624L, 8360L, 12360L, 8264L, 8256L, 16528L, 4168L, 16432L, 12432L, 12336L, 8440L, 4264L, 8336L, 4240L,
          8240L, 4344L, 4144L, 4160L, 4232L, 2619502664L, 1311060032L, 1317691456L, 1310900392L, 1309946008L, 1310306368L, 1309667480L, 1315876928L, 1307984024L, 2612047944L, 1307869248L, 1307828344L,
          1306751112L, 2610929736L, 1307771024L, 1306321032L, 1306976320L, 1306210352L, 1305817336L, 2610323528L, 2610319432L, 2610315336L, 1306206256L, 2610311240L, 1307537552L, 2610303048L,
          2610298952L, 1306103944L, 1306939456L, 2610278472L, 1307074680L, 1306976424L, 1306972328L, 2610274376L, 1305809144L, 1306370200L, 1306968232L, 1307033720L, 2610212936L, 1306038408L,
          1307005048L, 1306124336L, 2610159688L, 1306919080L, 1306026120L, 1307492496L, 1306116144L, 1306882112L, 1306120240L, 2610155592L, 1305755896L, 1306325144L, 1306321048L, 1305747704L,
          1307488400L, 1305743608L, 1306988664L, 2610147400L, 1306022024L, 1306910888L, 1306017928L, 1306980472L, 1305739512L, 1306906792L, 1306316952L, 1306009736L, 1305735416L, 1306308760L,
          2610114632L, 1306300568L, 1306964088L, 1305727224L, 1306095664L, 1306091568L, 1307476112L, 1305714936L, 1305993352L, 1307472016L, 1306955896L, 1305989256L, 1306837056L, 1306869928L,
          2610073672L, 1306062896L, 1307463824L, 1306951800L, 1306939512L, 1306943608L, 1306058800L, 1305686264L, 1306935416L, 2610069576L, 1307455632L, 1306271896L, 1306259608L, 1306267800L,
          1305985160L, 2610065480L, 1306857640L, 1306239128L, 2610024520L, 1306923128L, 1306919032L, 1305665784L, 1307439248L, 1307435152L, 1307431056L, 2610016328L, 1306800192L, 2610012232L,
          2609995848L, 1306218648L, 1306026032L, 1307418768L, 1306787904L, 1306820776L, 1305633016L, 2609942600L, 1306759232L, 1305899144L, 1306845304L, 1306140824L, 1306132632L, 1307349136L,
          1307332752L, 1305542904L, 2609741896L, 1306693800L, 1312997544L, 1306742904L, 1305518328L, 1306726520L, 1312309400L, 1304998136L, 1305161864L, 2608513096L, 2608484424L, 1305129096L,
          1305981096L, 1304973560L, 1305428120L, 2608468040L, 1305161776L, 1306022008L, 2608455752L, 1304957176L, 1305108616L, 1305997432L, 2608390216L, 1305993280L, 1305989184L, 2608349256L,
          1304940792L, 1305071752L, 1305067656L, 1306456208L, 1305378968L, 1306448016L, 1305129008L, 2608312392L, 1305055368L, 1305980992L, 1305903272L, 2608316488L, 1306435728L, 2608308296L,
          1305956472L, 2608300104L, 2608296008L, 1306431632L, 1305976896L, 1305370776L, 1305362584L, 2608291912L, 1305051272L, 1305116720L, 2608283720L, 2608279624L, 1304895736L, 1305338008L,
          2608275528L, 1305038984L, 2608263240L, 1305325720L, 1305882792L, 1305878696L, 1305923704L, 1304879352L, 1305305240L, 2608250952L, 1305309336L, 2608242760L, 1305944128L, 1305083952L,
          1305858216L, 1305919608L, 2608246856L, 1304875256L, 1305010312L, 2608234568L, 1305002120L, 1306402960L, 1306398864L, 1304993928L, 1306394768L, 1305940032L, 1304998024L, 1304867064L,
          1305067568L, 1305063472L, 1305059376L, 2608222280L, 1306386576L, 1304989832L, 1305931840L, 2608218184L, 2608214088L, 1305911416L, 1305841832L, 2608201800L, 1305837736L, 1305292952L,
          1306382480L, 1304862968L, 2608209992L, 2608193608L, 1305055280L, 1305923648L, 1305288856L, 1306378384L, 1304858872L, 1305051184L, 2608181320L, 2608177224L, 2608185416L, 1306362000L,
          2608169032L, 1306370192L, 1305284760L, 1305829544L, 1305833640L, 1306357904L, 1305903224L, 1305825448L, 1305276568L, 2608164936L, 1304973448L, 1305280664L, 1305821352L, 1306349712L,
          1305042992L, 1305903168L, 1306353808L, 2608156744L, 2608160840L, 1305272472L, 1305038896L, 1306345616L, 1305899128L, 1305268376L, 2608152648L, 2608148552L, 1306341520L, 1304846584L,
          1305034800L, 1304965256L, 1305899072L, 1304842488L, 2608136264L, 2608132168L, 1305260184L, 1306337424L, 1305256088L, 2608123976L, 1305251992L, 1305809064L, 1304830200L, 1305890936L,
          1305800872L, 1305804968L, 1305026608L, 1305894976L, 1304826104L, 1305796776L, 1305882688L, 1306333328L, 1304957064L, 2608115784L, 1306329232L, 1305022512L, 1305882744L, 1304952968L,
          1306321040L, 1305792680L, 2608107592L, 2608111688L, 1304822008L, 1306316944L, 1305878648L, 1304817912L, 2608103496L, 1305874552L, 1306312848L, 2608099400L, 1305870456L, 1305239704L,
          2608091208L, 1304948872L, 1306308752L, 1304813816L, 2608095304L, 1305014320L, 2608083016L, 1305784488L, 1305788584L, 1305874496L, 1305018416L, 1304944776L, 1306304656L, 1305866360L,
          1305002032L, 1305010224L, 2608074824L, 1305235608L, 1304993840L, 1304997936L, 2608070728L, 1305858168L, 1304940680L, 1306300560L, 2608066632L, 1304809720L, 2608058440L, 2608062536L,
          2608054344L, 1304985648L, 1305870400L, 1304936584L, 1304805624L, 2608050248L, 1305780392L, 1304977456L, 2608046152L, 1305231512L, 1304932488L, 2608042056L, 1305223320L, 2608037960L,
          1305854072L, 1306296464L, 1305227416L, 1304789240L, 1305849976L, 1304928392L, 2608033864L, 1304793336L, 1304969264L, 2608025672L, 1305768104L, 1305845880L, 2608029768L, 1305219224L,
          1305772200L, 1304965168L, 1305862208L, 1304785144L, 1306292368L, 2608021576L, 1305759912L, 1304961072L, 1305854016L, 1305215128L, 1305858112L, 1304924296L);
      /** 如果从ES里面获取的Department的ID信息为0, 说明已经执行完成 */
      if (0 == ids.size()) {
        break;
      } else {
        System.out.println("第" + page + "页!");
        System.out.println("ids::::::" + ids.size());
      }
      // @Query("match p=(a:b:Department)-[:PAYMENT]-(b:Company) where b.state
      // <> '注销企业' return a as startnode ,nodes(p) as nodes,rels(p) as
      // links,length(p) as length order by a.money desc limit 200")
      ids.parallelStream().forEach((id) -> {
        // 关系
        List<Map<String, Object>> relations = new ArrayList<>();
        // 出发节点
        CacheVertex fromVertex = null;
        // 关联的点
        HashSet<Long> vertices = new HashSet<Long>();
        // Department指出的总金额
        Double money = 0.0;
        // 查找关系开始
        GraphTraversal<Vertex, Map<String, Object>> result = g.V(id).has("type", "Department").as("a").outE().has("type", "PAYMENT").as("r").inV().has("type", "Company").has("state", P.neq("注销企业"))
            .as("b").select("a", "b").dedup();
        // 处理所有节点信息
        while (result.hasNext()) {
          Map<String, Object> r = result.next();
          if (null == fromVertex) {
            fromVertex = (CacheVertex) r.get("a");
            vertices.add((Long) fromVertex.id());
          }
          vertices.add((Long) ((CacheVertex) r.get("b")).id());
        }
        // 如果有点, 则进行关系处理
        if (null != fromVertex && 0 < vertices.size()) {
          // 处理金额
          GraphTraversal<Vertex, Object> moneyTemps = g.V(id).has("type", "Department").as("a").outE().has("type", "PAYMENT").as("r").inV().has("type", "Company").has("state", P.neq("注销企业")).as("b")
              .select("r").dedup().values("money");
          temp.add(id);
          while (moneyTemps.hasNext()) {
            try {
              Object moneyTemp = moneyTemps.next();
              if (moneyTemp instanceof Long) {
                money += Double.valueOf((Long) moneyTemp);
              } else if (moneyTemp instanceof Integer) {
                money += Double.valueOf((Integer) moneyTemp);
              } else if (moneyTemp instanceof Double) {
                money += (Double) moneyTemp;
              } else if (moneyTemp instanceof Float) {
                money += (Float) moneyTemp;
              } else if (moneyTemp instanceof String) {
                money += Double.valueOf((String) moneyTemp);
              } else {
                ;
              }
            } catch (Exception e) {
              ;
            }
          }
          // 处理关系
          GraphTraversal<Vertex, Object> countTemp = g.V(id).has("type", "Department").as("a").outE().has("type", "PAYMENT").as("r").inV().has("type", "Company").has("state", P.neq("注销企业")).as("b")
              .select("r");
          ArrayList<CacheEdge> cs = new ArrayList<>();
          while (countTemp.hasNext()) {
            cs.add((CacheEdge) countTemp.next());
          }
          vertices.clear();
          // 对关系进行过滤, 取15~20个
          cs.parallelStream().filter((CacheEdge s) -> {
            double ss = s.value("money");
            return ss > 10000;
          }).sorted((CacheEdge s, CacheEdge e) -> {
            double ss = s.value("money");
            double ee = e.value("money");
            if (ss > ee) {
              return -1;
            } else if (ss < ee) {
              return 1;
            } else {
              return 0;
            }
          }).limit(15 + new Random().nextInt(5)).forEach((CacheEdge r) -> {
            // 获取关系中用的点
            org.janusgraph.graphdb.relations.RelationIdentifier edgeid = r.id();
            long fvid = edgeid.getOutVertexId();
            long tvid = edgeid.getInVertexId();
            vertices.add(fvid);
            vertices.add(tvid);
          });
          if( 0 >= vertices.size()){
            return;
          }
          // 重新筛选
          countTemp = g.V(id).has("type", "Department").as("a").bothE().as("r").otherV().hasId(vertices.toArray()).as("b").select("r");
          cs.clear();
          while (countTemp.hasNext()) {
            cs.add((CacheEdge) countTemp.next());
          }
          // fvid tvid type count
          Map<Long, Map<Long, Map<String, Long>>> count = new HashMap<>();
          cs.forEach((CacheEdge r) -> {
            org.janusgraph.graphdb.relations.RelationIdentifier edgeid = r.id();
            String type = r.label();
            long fvid = edgeid.getOutVertexId();
            long tvid = edgeid.getInVertexId();
            // fvid
            if (count.containsKey(fvid)) {
              // tvid type count fvid
              Map<Long, Map<String, Long>> _2 = count.get(fvid);
              // fvid
              if (_2.containsKey(tvid)) {
                // type count fvid
                Map<String, Long> _3 = _2.get(tvid);
                // type
                if (_3.containsKey(type)) {
                  // type count
                  _3.put(type, _3.get(type) + 1);
                } else {
                  // type count
                  _3.put(type, 1L);
                }
              } else {
                // type count
                Map<String, Long> map = new HashMap<String, Long>();
                // type count
                map.put(type, 1L);
                // tvid <type,count>
                _2.put(tvid, map);
              }
            } else {
              // tvid type count
              Map<Long, Map<String, Long>> _2 = new HashMap<>();
              // type count
              Map<String, Long> _3 = new HashMap<>();
              // type count
              _3.put(type, 1L);
              // tvid <type,count>
              _2.put(tvid, _3);
              // fvid <tvid,<type,count>>
              count.put(fvid, _2);
            }
          });
          count.forEach((Long fvid, Map<Long, Map<String, Long>> _2) -> {
            _2.forEach((Long tvid, Map<String, Long> _3) -> {
              _3.forEach((String type, Long _4_count) -> {
                Map<String, Object> link = new HashMap<>();
                link.put("fvid", fvid);
                link.put("tvid", tvid);
                link.put("type", type);
                link.put("count", _4_count);
                relations.add(link);
              });
            });
          });
          Iterator<VertexProperty<Object>> uidT = fromVertex.properties("uid");
          Iterator<VertexProperty<Object>> nameT = fromVertex.properties("name");
          Iterator<VertexProperty<Object>> timeT = fromVertex.properties("time");
          String uid = "";
          String name = "";
          String time = "";
          if (uidT.hasNext()) {
            uid = (String) uidT.next().value();
          }
          if (nameT.hasNext()) {
            name = (String) nameT.next().value();
          }
          if (timeT.hasNext()) {
            time = (String) timeT.next().value();
          }
          addES(id, uid, name, time, money, vertices, relations);
        }
        size.addAndGet(1);
        if (size.get() % 100 == 0) {
          System.out.println("size::::" + size);
        }
      });
    }
    System.out.println(new Gson().toJson(temp));
    GraphFactory.getInstance().close();
  }

  /**
   * 
   * @param id
   *          顶点id
   * @param uid
   *          顶点uid
   * @param name
   *          顶点name
   * @param time
   *          注册时间
   * @param money
   *          注资金额
   * @param nodes
   *          牵扯到节点信息
   * @param links
   *          牵扯到的关系信息
   */
  public static final void addES(Long id, String uid, String name, String time, Double money, HashSet<Long> nodes, List<Map<String, Object>> links) {
    TransportClient client = ESConnection.getClient();
    IndexRequestBuilder requestBuilder = client.prepareIndex("janusgraph_all_gailantype2d", "a", LongEncoding.encode(id));
    Map<String, Object> source = new HashMap<>();
    source.put("id", id);
    source.put("uid", uid);
    source.put("name", name);
    source.put("time", time);
    try {
      source.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime());
    } catch (ParseException e) {
      // return;
    }
    source.put("money", money);

    nodes.clear();

    links.forEach((Map<String, Object> m) -> {
      nodes.add((Long) m.get("fvid"));
      nodes.add((Long) m.get("tvid"));
    });
    if (0.0 >= money || 0 == nodes.size() || 0 == links.size()) {
      return;
    }

    source.put("nodes", nodes);
    source.put("links", links);
    source.put("updatetime", System.currentTimeMillis());
    source.put("type", "GaiLanTypeIsTwoForD");
    requestBuilder.setSource(source);
    requestBuilder.execute().actionGet();
    System.out.println("-----------------------------------------------");
    System.out.println(new Gson().toJson(source));
    System.out.println("-----------------------------------------------");
  }

  /**
   * 获取所有Department的ID
   * 
   * @param ids
   */
  public static final void esGet(List<Long> ids, Integer page) {
    System.out.println("开始拉取ES数据");
    final Integer size = 3000000;
    TransportClient client = ESConnection.getClient();
    SearchRequestBuilder prepareSearch = client.prepareSearch().setIndices("janusgraph_all_vertex").setTypes("all_vertex");
    prepareSearch.setFrom(page * size).setSize(size);
    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.matchPhraseQuery("type", "Department"));
    boolQuery.mustNot(QueryBuilders.matchPhraseQuery("state__STRING", "注销企业"));
    prepareSearch.setQuery(boolQuery);
    prepareSearch.addSort("updatetime", SortOrder.DESC);
    prepareSearch.storedFields();
    SearchResponse searchResponse = prepareSearch.get();
    SearchHits searchHits = searchResponse.getHits();
    searchHits.forEach((SearchHit hits) -> {
      ids.add(LongEncoding.decode(hits.getId()));
    });
    System.out.println("结束拉取ES数据");
  }

}