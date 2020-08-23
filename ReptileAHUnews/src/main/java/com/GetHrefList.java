package com;

/**
 * @author Jianshu
 * @time 20200823
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 *该类主是获取在日期范围内的所有新闻的链接，并保存在List集合中去
 */
public class GetHrefList {
    String mainURL="http://news.ahu.edu.cn";//由于安达新闻内网的所有的跳转链接都没有根域名，所以需要自己拼凑完整域名
    List<String> LinkHrefList=new ArrayList<>();//设置一个集合用来存放所有在日期范围内的新闻链接
    String pageHref;//首页

    public GetHrefList(String u){
        pageHref=u;
    }

    public List<String> getAllPageHrefList(){
        int sum=0;
        while(true){
            if(getPerPageHrefList(pageHref)==1){
                sum++;//爬取的页码总数
            }
            //当爬取的页数不为0而且当前获得页为空，则判断到了末尾，否则一直爬取（sum为0，当前页为空时，说明一直在前面没爬到范围内）
            if(sum>0&&getPerPageHrefList(pageHref)==0){
                break;
            }
            pageHref=getNextPage(pageHref);//得到下一个页面的超链接
        }
        return LinkHrefList;
    }

    public String getNextPage(String url){
        Document doc=null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        String nextPage=mainURL+doc.getElementsByClass("next").attr("href");//取出下一页链接，并和根域名组合成正确的超链接
        System.out.println("该页链接已经提取----["+nextPage+"]");
        return nextPage;
    }

    public int getPerPageHrefList(String url){
        int isPageNull=0;//判断该页是否获取到了范围内的列表
        Document doc=null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        Element articleList=doc.getElementsByClass("wp_article_list").get(0);//获取整个新闻列表块

        Elements articleLi=articleList.getElementsByTag("li");//将新闻块的每个li标签写到集合Elements
        //li标签内部进行解析
        //以下均为对网页DOM的解析
        for(Element listItem:articleLi){
            String liDate=listItem.getElementsByClass("fields ex_fields").text();
            //每个链接都是固定的格式而且超链接里面就有日期信息，因此，我们可以在超链接里面直接把日期信息截取出来，用来判断是否在日期内
            int dataNum=Integer.parseInt(liDate.substring(5,7)+liDate.substring(8,10));
            //直接提取日期并转换为int，与范围比较大小即可
            if(dataNum<=620&&dataNum>=201){
                Element content=listItem.getElementsByClass("Article_Title").get(0);
                Elements TagA=content.getElementsByTag("a");
                String linkHref = TagA.attr("href");
                //有两种链接，一种是校内链接需要拼接字符串，另一种是外链不需要拼接（由于外联有多种类型，该项目只解析内链）
                if(linkHref.substring(1,5).equals("2020")){
                    LinkHrefList.add(mainURL+linkHref);
                }
                else{
                    //LinkHrefList.add(linkHref);
                }
                isPageNull=1;
            }
        }
        return isPageNull;
    }
}

