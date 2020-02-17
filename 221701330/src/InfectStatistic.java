/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
import java.util.*;
import java.text.Collator;
import java.util.regex.*;
import  java.io.*;
import  java.lang.*;
class Province implements Comparable<Province>
{
    private String province;
    private int ip = 0,sp = 0,cure = 0,dead = 0;
    public Province(String province)
    {
        this.province = province;
    }
    public void setprovince(String pro)
    {
        this.province = pro;
    }

    public String getprovince()
    {
        return this.province;
    }
    public void setip(int ip)
    {
        this.ip = ip;
    }
    public int getip()
    {
        return this.ip;
    }
    public void setsp(int sp)
    {
        this.sp = sp;
    }
    public int getsp()
    {
        return this.sp;
    }
    public void setcure(int cure)
    {
        this.cure = cure;
    }
    public int getcure()
    {
        return this.cure;
    }
    public void setdead(int dead)
    {
        this.dead = dead;
    }
    public int getdead()
    {
        return this.dead;
    }
    public int compareTo(Province a)
    {
        String province1 = this.province;
        String province2 = a.province;
        Collator instance = Collator.getInstance(Locale.CHINA);
        if(province1.indexOf("重") >= 0){
            province1 = province1.replaceAll("重", "冲");//多音字重chong庆
        }
        if(province2.indexOf("重") >= 0){
            province2 = province2.replaceAll("重", "冲");
        }
        if(province1.equals("全国")) return -1; // 全国排第一
        else if(province2.equals("全国")) return 1;
        return instance.compare(province1,province2);  //按拼音排序
    }
}

class InfectStatistic {

    public static void addlist(String provence,List<Province> proList) // 判断列表中有没有该省份，无则加入
    {
        boolean inlist = false;
        for (Province province : proList) {
            inlist = provence.equals(province.getprovince());
            if(inlist) break ;
        }
        if(!inlist) // 若列表里没有该省份，加入列表
        {
            Province pro1 = new Province(provence);
            proList.add(pro1);
        }
    }

    public static void InformationProcessing(String Path,List<Province> proList,Province allpro) //日志信息处理
    {
        int people;
        String pattern = ".*//.*";
        String pattern0 = "(\\d+)";
        String pattern0_1 = ".*感染患者.*";
        String pattern0_2 = ".*疑似患者.*";
        String pattern1 = ".*新增.*";
        String pattern2 = ".*流入.*";
        String pattern3 = ".*死亡.*";
        String pattern4 = ".*治愈.*";
        String pattern5 = ".*确诊感染.*";
        String pattern6 = ".*排除.*";
        Pattern number = Pattern.compile(pattern0); // 创建 Pattern 对象
        try
        {
            File file = new File(Path);
            if(!file .exists())
            {
                System.out.println("路径错误");
                System.exit(0);
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = null;
            while ((str = br.readLine()) != null)
            {
                Matcher num = number.matcher(str); // 现在创建 matcher 对象
                String arrays[] = str.split(" ");
                if(Pattern.matches(pattern,str)) continue; // 跳过注释内容
                addlist(arrays[0],proList);
                for (Province province1 : proList) {
                    if (arrays[0].equals(province1.getprovince())&&num.find())
                    {
                        people = Integer.valueOf(num.group(0));
                        if(Pattern.matches(pattern1,str)) // 新增
                        {
                            if(Pattern.matches(pattern0_1,str)) // 新增感染患者
                            {
                                province1.setip(province1.getip() + people);
                                allpro.setip(allpro.getip() + people);
                            }
                            else if(Pattern.matches(pattern0_2,str)) //新增疑似患者
                            {
                                province1.setsp(province1.getsp() + people);
                                allpro.setsp(allpro.getsp() + people);
                            }

                        }
                        else if(Pattern.matches(pattern2,str)) //从省一流入省二
                        {
                            addlist(arrays[3],proList); //判断省二有没有在列表里边
                            for (Province province2 : proList) {
                                if (arrays[3].equals(province2.getprovince()))
                                {
                                    if(Pattern.matches(pattern0_1,str))
                                    {
                                        province1.setip(province1.getip() - people);
                                        province2.setip(province2.getip() + people);
                                    }
                                    else if(Pattern.matches(pattern0_2,str))
                                    {
                                        province1.setsp(province1.getsp() - people);
                                        province2.setsp(province2.getsp() + people);
                                    }
                                }
                            }
                        }
                        else if(Pattern.matches(pattern3,str)) //死亡 dead+n,ip-n
                        {
                            province1.setdead(province1.getdead() + people);
                            province1.setip(province1.getip() - people);
                            allpro.setdead(allpro.getdead() + people);
                            allpro.setip(allpro.getip() - people);
                        }
                        else if(Pattern.matches(pattern4,str)) //治愈 cure+n,ip-n
                        {
                            province1.setcure(province1.getcure() + people);
                            province1.setip(province1.getip() - people);
                            allpro.setcure(allpro.getcure() + people);
                            allpro.setip(allpro.getip() - people);
                        }
                        else if(Pattern.matches(pattern5,str)) //确诊感染 sp-n,ip+n
                        {
                            province1.setip(province1.getip() + people);
                            province1.setsp(province1.getsp() - people);
                            allpro.setip(allpro.getip() + people);
                            allpro.setsp(allpro.getsp() - people);
                        }
                        else if(Pattern.matches(pattern6,str)) //排除 sp-n
                        {
                            province1.setsp(province1.getsp() - people);
                            allpro.setsp(allpro.getsp() - people);
                        }
                    }
                }
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public static void InformationOut(String out,List<Province> proList,List<String> outinformation) //写出日志信息
    {
        out = out.trim();
        String Path = out.substring(0,out.lastIndexOf("\\")); //分离得到路径
        try {
            File file = new File(Path);
            if (!file.exists())
            {
                System.out.println("路径错误");
                System.exit(0);
            }
            BufferedWriter wr = new BufferedWriter(new FileWriter(out));
            for (Province province : proList){
                if (outinformation.isEmpty())
                {
                    wr.write(province.getprovince() + "共有感染患者" + province.getip() + "人 疑似患者" +province.getsp()
                            + "人 治愈" + province.getcure() + "人 死亡" + province.getdead() + "人\n");
                }
                else
                {
                    wr.write(province.getprovince() + "共有");
                    for (String outthing : outinformation)
                    {
                        if(outthing.equals("ip"))  wr.write("感染患者" + province.getip() + "人 ");
                        if(outthing.equals("sp")) wr.write("疑似患者" + province.getsp() + "人 ");
                        if(outthing.equals("cure")) wr.write("治愈" + province.getcure() + "人 ");
                        if(outthing.equals("dead")) wr.write("死亡" + province.getdead() + "人 ");
                    }
                    wr.write("\n");
                }
            }
            wr.write("  // 该文档并非真实数据，仅供测试使用");
            wr.flush();
            wr.close();
        }catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public static void getAllFileName(String path,ArrayList<String> listFileName) //得到日志名字
    {
        String patterndate = "\\d+-\\d+-\\d+";
        Pattern date = Pattern.compile(patterndate); // 创建 Pattern 对象
        File file = new File(path);
        String [] names = file.list();
        if(names != null){
            String [] completNames = new String[names.length];
            for(int i=0;i < names.length;i++){
                Matcher Date = date.matcher(names[i]); // 创建 matcher 对象
                if(Date.find()) completNames[i] = Date.group(0);
            }
            listFileName.addAll(Arrays.asList(completNames));
        }
    }
    public static void main(String[] args)
    {
        String log = null,date = null,out = null;
        int listjudge1 = 0 ,listjudge2 = 0;
        List<Province> proList = new ArrayList<>();
        ArrayList<String> listFileName = new ArrayList<>();
        ArrayList<String> outinformation = new ArrayList<String>();
        Province allpro = new Province("全国"); // 创建全国对象
        proList.add(allpro);
        for(int j = 0;j < args.length;j++) {
            if(args[j].equals("list")) listjudge1++;
            else if(args[j].equals("-log"))
            {
                log = args[j + 1 ];
                listjudge1++;
            }
            else if(args[j].equals("-out"))
            {
                out = args[j + 1];
                listjudge1++;
            }
            else if(args[j].equals("-date")) date = args[j + 1];
            else if(args[j].equals("-type")) listjudge2 = 1;
            else if(args[j].equals("ip")||args[j].equals("sp")||args[j].equals("cure")
                    ||args[j].equals("dead")) outinformation.add(args[j]);
        }
        if(listjudge1!=3||listjudge2!=1)

        {
            System.out.println("命令错误");
            System.exit(0);
        }
        getAllFileName(log,listFileName);
        for(String name:listFileName)
        {
            if(date==null)
            {
                InformationProcessing(log+name+".log.txt",proList,allpro);
                continue;
            }
            else if(listFileName.get(listFileName.size()-1).compareTo(date)<=0) //若输入的日期超出最新的日志范围
            {
                System.out.println("输入的日期有错");
                System.exit(0);
            }
            else if (listFileName.get(0).compareTo(date) > 0) break; //若输入日期小于最旧的日志
            else if(name.compareTo(date) > 0) break;
            InformationProcessing(log+name+".log.txt",proList,allpro);
        }
        Collections.sort(proList);
        InformationOut(out,proList,outinformation);
    }
}
