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
    String province=null;
    int ip = 0,sp = 0,cure = 0,dead = 0;
    public Province(String province,int ip,int sp,int cure,int dead)
    {
        this.province = province;
        this.ip = ip;
        this.sp = sp;
        this.cure = cure;
        this.dead = dead;
    }

    public Province() {}

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
        return instance.compare(province1,province2);  //按拼音排序
    }

    public void printmessage()
    {
        System.out.println("province:"+province+" ip:"+ip+" sp:"+sp+" cure:"+cure+" dead:"+dead);
    }
}

class InfectStatistic {
    public static Boolean wronglist1(String args[])
    {
        boolean wrong = false;
        return wrong;
    }

    public static void Log(String Path)
    {
        String pattern = ".*//.*";
        boolean isMatch;
        try
        {
            File file = new File(Path);
            if(!file .exists()) {
                System.out.println("文件不存在");
                //System.exit(0);
            }
            else{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String str = null;
                while ((str = br.readLine()) != null) {
                    if(Pattern.matches(pattern,str)) continue;
                    System.out.println("读取的内容为：" + str);
                }
            }
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }
    public static void getAllFileName(String path,ArrayList<String> listFileName){
        File file = new File(path);
        File [] files = file.listFiles();
        String [] names = file.list();
        if(names != null){
            String [] completNames = new String[names.length];
            for(int i=0;i<names.length;i++){
                completNames[i]=path+names[i];
            }
            listFileName.addAll(Arrays.asList(completNames));
        }
    }
    public static void main(String[] args)
    {
        String pattern4 = "-date";
        String pattern5 = "-type";
        String log = null,date = null,out = null;
        int listjudge = 0;
        for(int j = 0;j<args.length;j++) {
            if(args[j].equals("list")) listjudge++;
            else if(args[j].equals("-log"))
            {
                log = args[j+1];
                listjudge++;
            }
            else if(args[j].equals("-out"))
            {
                out = args[j+1];
                listjudge++;
            }
            else if(args[j].equals("-date")) date = args[j+1];
        }
        if(listjudge!=3)
        {
            System.out.println("命令错误");
            //System.exit(0);
        }
        ArrayList<String> listFileName = new ArrayList<String>();
        getAllFileName(log,listFileName);
        for(String name:listFileName){
            if(name.contains(".txt")||name.contains(".properties")){
                Log(name);
                if(date==null) continue;
                else if(name.equals(log+date+".log.txt")) break;
            }
        }
        List<Province> proList = new ArrayList<>();
        Province pro1 = new Province("北京",3,4,5,6);
        Province pro2 = new Province("重庆",5,5,5,5);
        Province pro3 = new Province("湖北",1,2,3,4);
        proList.add(pro1);
        proList.add(pro2);
        proList.add(pro3);
        Collections.sort(proList);
        for (Province province : proList) {
            province.printmessage();
        }
        System.out.println(listjudge);
    }
}
