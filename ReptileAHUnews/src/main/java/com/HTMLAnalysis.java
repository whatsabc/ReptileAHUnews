package com;

/**
 * @author Jianshu
 * @time 20200823
 */
import com.Utils.MapUtils;
import com.huaban.analysis.jieba.JiebaSegmenter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

/*
 *该类主要是对获得的新闻链接依次访问并解析，然后使用结巴分词处理解析到的文本，将解析结果存到TXT文件
 */
public class HTMLAnalysis {
    List<String> hrefList;
    List<String> twoWordsDict=new ArrayList<>();//存放结巴分词后的二字词
    List<String> fourWordsDict=new ArrayList<>();//存放结巴分词后的四字词

    public HTMLAnalysis(List<String> l){
        hrefList=l;
    }
    public void Analysis(){
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<String> dictList=segmenter.sentenceProcess(getText());//结巴分词后的所有词语先暂时存放在这个List中
        //根据分词的长度，依次区分里面全是汉字的二字词和四字词
        for(String s:dictList){
            int flag=1;
            if(s.length()==2||s.length()==4){
                for(int i=0;i<s.length();i++){
                    int n = (int)s.charAt(i);
                    //如果不在汉字的编码范围，说明是符号分词或者其他分词，跳过
                    if(!(19968 <= n && n <40869)) {
                        flag=0;
                    }
                }
                //依次存放入两个集合中
                if(flag==1){
                    if(s.length()==2){
                        twoWordsDict.add(s);
                    }
                    if(s.length()==4){
                        fourWordsDict.add(s);
                    }
                }
            }
        }
        //进行词频统计
        Map<String, Integer> twoWordsMap = MapUtils.MapStatistics(twoWordsDict);
        Map<String, Integer> fourWordsMap = MapUtils.MapStatistics(fourWordsDict);
        //将词频统计按照降序排列
        List<Map.Entry<String, Integer>> list2=MapUtils.MapSort(twoWordsMap);
        List<Map.Entry<String, Integer>> list4=MapUtils.MapSort(fourWordsMap);
        //写入到文本文件中
        toTxtFile(list2,"token2.txt");
        toTxtFile(list4,"token4.txt");
    }

    public String getText(){
        String article="";
        Document doc=null;
        for(String url:hrefList){
            System.out.println("正在提取该网页新闻----["+url+"]");
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Element articleDiv=doc.getElementsByClass("wp_articlecontent").get(0);//获取整个新闻块
            Elements articleSump=articleDiv.getElementsByTag("p");//将所有div块的p段落的text全部取出来
            article=article+articleSump.text();//所有页面的字符循环都存入一个String内（查阅得知，String对象在运行时最多可以存放4GB数据）
        }
        return article;
    }

    public void toTxtFile(List<Map.Entry<String, Integer>> list,String f){
        FileWriter fos = null;//文件写入流
        String fileName="C:\\Users\\Jianshu\\Desktop\\"+f;
        try {
            fos = new FileWriter(fileName);
            fos.write("------[词语统计结果]------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对hashmap进行循环遍历依次取出键和值
        for (Map.Entry<String, Integer> mapping : list){
            System.out.println(mapping.getKey()+": "+mapping.getValue());
            try {
                fos.write(mapping.getKey()+": "+mapping.getValue()+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
