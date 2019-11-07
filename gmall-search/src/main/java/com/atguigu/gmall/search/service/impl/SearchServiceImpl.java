package com.atguigu.gmall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.search.service.SearchService;
import com.atguigu.gmall.search.vo.GoodsVO;
import com.atguigu.gmall.search.vo.SearchParamVO;
import com.atguigu.gmall.search.vo.SearchResponse;
import com.atguigu.gmall.search.vo.SearchResponseAttrVO;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.ChildrenAggregation;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by 12441 on 2019/11/6
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private JestClient jestClient;

    @Override
    public void search(SearchParamVO searchParamVO) {

        try {
            String dsl = buildDSL(searchParamVO);
            System.out.println(dsl);
            Search search = new Search.Builder(dsl).addIndex("goods").addType("info").build();

            SearchResult searchResult = this.jestClient.execute(search);
            SearchResponse response = parseResult(searchResult);
            //分页参数
            response.setPageSize(searchParamVO.getPageSize());
            response.setPageNum(searchParamVO.getPageNum());
            response.setTotal(searchResult.getTotal());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SearchResponse parseResult(SearchResult result) {

        SearchResponse response = new SearchResponse();
        //获取所有聚合
        MetricAggregation aggregations = result.getAggregations();
        //解析品牌的聚合结果集
        //获取品牌的聚合
        TermsAggregation brandAgg = aggregations.getTermsAggregation("brandAgg");
        //获取品牌聚合的所有桶
        List<TermsAggregation.Entry> buckets = brandAgg.getBuckets();
        //判断品牌聚合是否为空
        if (!CollectionUtils.isEmpty(buckets)){
            //初始化品牌vo对象
            SearchResponseAttrVO attrVO = new SearchResponseAttrVO();
            attrVO.setName("品牌");//写死品牌聚合名称
            List<String> brandValues = buckets.stream().map(bucket -> {
                Map<String, Object> map = new HashMap<>();
                //拿到按brandid聚合的结果集
                map.put("id",bucket.getKeyAsString());
                TermsAggregation brandNameAgg = bucket.getTermsAggregation("brandNameAgg");//获取品牌id桶中子聚合
                //由于brandid和brandName一一对应，所有只有一个brandName
                map.put("name",brandAgg.getBuckets().get(0).getKeyAsString());
                return JSON.toJSONString(map);
            }).collect(Collectors.toList());
            attrVO.setValue(brandValues);//设置品牌的所有聚合值
            response.setBrand(attrVO);
        }
        //解析分类的聚合结果集
        TermsAggregation categoryAgg = aggregations.getTermsAggregation("categoryAgg");
        //获取分类聚合的所有桶
        List<TermsAggregation.Entry> catBuckets = categoryAgg.getBuckets();
        //判断分类聚合是否为空
        if (!CollectionUtils.isEmpty(catBuckets)){
            SearchResponseAttrVO categoryVO = new SearchResponseAttrVO();
            categoryVO.setName("分类");
            List<String> categoryValues = catBuckets.stream().map(bucket -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", bucket.getKeyAsString());
                TermsAggregation categoryNameAgg = bucket.getTermsAggregation("categoryNameAgg");
                map.put("name", categoryNameAgg.getBuckets().get(0).getKeyAsString());
                return JSON.toJSONString(map);
            }).collect(Collectors.toList());
            categoryVO.setValue(categoryValues);
            response.setCatelog(categoryVO);
        }
        //解析搜索属性的聚合结果集
        ChildrenAggregation attrAgg = aggregations.getChildrenAggregation("attrAgg");
        TermsAggregation attrIdAgg = attrAgg.getTermsAggregation("attrIdAgg");
        List<SearchResponseAttrVO> attrVOS = attrIdAgg.getBuckets().stream().map(bucket -> {
            SearchResponseAttrVO attrVO = new SearchResponseAttrVO();
            attrVO.setProductAttributeId(Long.valueOf(bucket.getKeyAsString()));
            //获取搜索属性的子聚合(搜索属性名)
            TermsAggregation attrNameAgg = bucket.getTermsAggregation("attrNameAgg");
            attrVO.setName(attrNameAgg.getBuckets().get(0).getKeyAsString());
            //获取搜索属性的子聚合(搜索属性)
            TermsAggregation attrValueAgg = bucket.getTermsAggregation("attrValueAgg");
            List<String> values = attrValueAgg.getBuckets().stream().map(bucket1 -> bucket1.getKeyAsString()).collect(Collectors.toList());
            attrVO.setValue(values);
            return attrVO;
        }).collect(Collectors.toList());
        response.setAttrs(attrVOS);
        //解析商品列表的结果集
        List<GoodsVO> goodsVO = result.getSourceAsObjectList(GoodsVO.class, false);
        response.setProducts(goodsVO);
        return response;
    }

    private String buildDSL(SearchParamVO searchParamVO) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //1. 构建查询和过滤条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        String keyword = searchParamVO.getKeyword();
        if (StringUtils.isNotEmpty(keyword)){
            boolQuery.must(QueryBuilders.matchQuery("name",keyword).operator(Operator.AND));
        }
        //构建过滤条件
        //品牌
        String[] brands = searchParamVO.getBrand();
        if (ArrayUtils.isNotEmpty(brands)) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId",searchParamVO.getBrand()));
        }
        //分类
        String[] catelog3 = searchParamVO.getCatelog3();
        if (ArrayUtils.isNotEmpty(catelog3)){
            boolQuery.filter(QueryBuilders.termsQuery("productCategoryId",catelog3));
        }
        //搜索的规格属性过滤
        String[] props = searchParamVO.getProps();
        if (ArrayUtils.isNotEmpty(props)){
            for (String prop : props) {
                String[] attr = org.apache.commons.lang3.StringUtils.split(prop, ":");
                if (attr!=null&&attr.length==2){
                   BoolQueryBuilder propBoolQuery = QueryBuilders.boolQuery();
                   propBoolQuery.must(QueryBuilders.termQuery("attrValueList.productAttributeId",attr[0]));
                    String[] values = org.apache.commons.lang3.StringUtils.split(attr[1], "-");
                    propBoolQuery.must(QueryBuilders.termsQuery("attrValueList.value",values));
                    boolQuery.filter(QueryBuilders.nestedQuery("attrValueList",propBoolQuery, ScoreMode.None));
                }
            }
        }
        sourceBuilder.query(boolQuery);

        //2. 完成分页的构建
        Integer pageNum = searchParamVO.getPageNum();
        Integer pageSize = searchParamVO.getPageSize();
        sourceBuilder.from((pageNum-1)*pageSize);
        sourceBuilder.size(pageSize);

        //3. 完成排序的构建
        String order = searchParamVO.getOrder();
        if (StringUtils.isNotBlank(order)){
            String[] orders = org.apache.commons.lang3.StringUtils.split(order, ":");
            if (orders!=null&&orders.length==2){
                SortOrder sortOrder = StringUtils.equals("asc", orders[1]) ? SortOrder.ASC : SortOrder.DESC;
                switch (orders[0]){
                    case "0":sourceBuilder.sort("_score",sortOrder);
                    case "1":sourceBuilder.sort("sale",sortOrder);
                    case "2":sourceBuilder.sort("price",sortOrder);
                    default:break;
                }
            }

        }
        //4. 完成高亮的构建
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        sourceBuilder.highlighter(highlightBuilder);
        //5. 完成聚合条件的构建
        //品牌
        sourceBuilder.aggregation(
          AggregationBuilders.terms("brandAgg").field("brandId")
                .subAggregation(AggregationBuilders.terms("brandNameAgg").field("brandName"))
        );
        //分类
        sourceBuilder.aggregation(
                AggregationBuilders.terms("categoryAgg").field("productCategoryId")
                        .subAggregation(AggregationBuilders.terms("categoryNameAgg").field("productCategoryName"))
        );
        //收索属性
        sourceBuilder.aggregation(
          AggregationBuilders.nested("attrAgg","attrValueList")
                .subAggregation(AggregationBuilders.terms("attrIdAgg").field("attrValueList.productAttributeId")
                        .subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrValueList.name"))
                        .subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrValueList.value"))
                )
        );

        return sourceBuilder.toString();
    }
}
