package com.me.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {

        //正则表达式可以用字符串来描述规则，并用来匹配字符串。
        //正则表达式是一套标准，它可以用于任何语言。
        // Java标准库的java.util.regex包内置了正则表达式引擎，在Java程序中使用正则表达式非常简单。
        String regex = "20\\d\\d";
        System.out.println("2019".matches(regex)); // true
        System.out.println("2100".matches(regex)); // false

        //正则表达式的匹配规则是从左到右按规则匹配
        //要注意正则表达式在Java代码中也是一个字符串，
        // 所以，对于正则表达式a\&c来说，对应的Java字符串是"a\\&c"，
        // 因为\也是Java字符串的转义字符，两个\\实际上表示的是一个\
        String re1 = "abc";
        System.out.println("abc".matches(re1));
        System.out.println("Abc".matches(re1));
        System.out.println("abcd".matches(re1));

        String re2 = "a\\&c"; // 对应的正则是a\&c
        System.out.println("a&c".matches(re2));
        System.out.println("a-c".matches(re2));
        System.out.println("a&&c".matches(re2));

        //配非ASCII字符，例如中文，那就用十六进制表示
        System.out.println(("和").matches("\u548c"));

        /**大多数情况下，我们想要的匹配规则更多的是模糊匹配。
         * 我们可以用 . 匹配 一个 任意 字符
         * \d 可以匹配 一个 数字字符
         * \w 可以匹配 一个 字母、数字或下划线
         * \s 可以匹配 一个 空白字符，注意空白字符不但包括空格，还包括tab字符（在Java中用\t表示）
         *而 \D 则匹配一个非数字，\W可以匹配\w不能匹配的字符，\S可以匹配\s不能匹配的字符，这几个正好是反着来的。
         *
         * 修饰符 * 可以匹配 任意个 字符，包括0个字符
         * 修饰符 + 可以匹配 至少一个 字符
         * 修饰符 ? 可以匹配 0个或一个 字符
         * 修饰符 {n} 可以精确指定 n个 字符
         * 修饰符 {n,m} 可以指定匹配 n~m个 字符
         * 修饰符 {n,} 可以匹配 至少n个 字符
         *
         * 正则表达式进行多行匹配时，我们用^表示开头，$表示结尾
         *
         * 使用[...]可以匹配范围内的字符，例如，[123456789]可以匹配1~9
         * 把所有字符全列出来太麻烦，[...]还有一种写法，直接写[1-9]就可以
         * [...]还有一种 排除法，即不包含指定范围的字符。假设我们要匹配任意字符，但不包括数字，可以写[^0-9]{3}：
         *
         * 用|连接的两个正则规则是 或规则，例如，AB|CD表示可以匹配AB或CD
         *
         * 规则太复杂了，可以把公共部分提出来，然后用 (...) 把子规则括起来，由此还可以用来分组解析
         */

        String re11 = "java\\d"; // 对应的正则是java\d
        System.out.println("java9".matches(re11));
        System.out.println("java10".matches(re11));
        System.out.println("javac".matches(re11));

        String re22 = "java\\D";
        System.out.println("javax".matches(re22));
        System.out.println("java#".matches(re22));
        System.out.println("java5".matches(re22));


        //国内的电话号码规则：3~4位区号加7~8位电话，中间用-连接，
        //国内区号必须以0开头，而电话号码不能以0开头
        String re = "0\\d{2,3}-[1-9]\\d{6,7}";
        for (String s : List.of("010-12345678", "020-9999999", "0755-7654321")) {
            if (!s.matches(re)) {
                System.out.println("测试失败: " + s);
                return;
            }
        }
        for (String s : List.of("010 12345678", "A20-9999999", "0755-7654.321")) {
            if (s.matches(re)) {
                System.out.println("测试失败: " + s);
                return;
            }
        }
        System.out.println("测试成功!");

        //或规则匹配
        String re3 = "java|php";
        System.out.println("java".matches(re3));
        System.out.println("php".matches(re3));
        System.out.println("go".matches(re3));

        //可以把公共部分提出来，然后用(...)把子规则括起来表示成learn\\s(java|php|go)。
        String re4 = "learn\\s(java|php|go)";
        System.out.println("learn java".matches(re4));
        System.out.println("learn Java".matches(re4));
        System.out.println("learn php".matches(re4));
        System.out.println("learn Go".matches(re4));

        String re5 = "learn\\s((j|J)ava|(p|P)hp|(g|G)o)";
        System.out.println("learn java".matches(re5));
        System.out.println("learn Java".matches(re5));
        System.out.println("learn php".matches(re5));
        System.out.println("learn Go".matches(re5));

        /**如何提取匹配的子串？
         * 用(...)先把要提取的规则 分组，把上述正则表达式变为(\d{3,4})\-(\d{6,8})。
         * 匹配后，如何按括号提取子串？
         * 现在我们没办法用String.matches()这样简单的判断方法了，必须引入java.util.regex包，用Pattern对象匹配，
         * 匹配后获得一个Matcher对象，如果匹配成功，就可以直接从 Matcher.group(index) 返回子串：
         */
        Pattern pattern = Pattern.compile("(\\d{3,4})-(\\d{7,8})");
        Matcher matcher = pattern.matcher("010-12345678");
        //使用Matcher时，必须首先调用matches()判断是否匹配成功，匹配成功后，才能调用group()提取子串。
        if (matcher.matches()) {

            //第0组是原字符串，groupCount是分割的组数（不包括第0组）
            for (int i = 0; i <= matcher.groupCount(); i++) {
                System.out.println(matcher.group(i));
            }
        } else {
            System.out.println("匹配失败！");
        }

        //反复使用String.matches()对同一个正则表达式进行多次匹配效率较低，因为每次都会创建出一样的Pattern对象。
        // 完全可以先创建出一个Pattern对象，然后反复使用，就可以实现编译一次，多次匹配：
        Pattern pattern1 = Pattern.compile("(\\d{3,4})-(\\d{7,8})");
        System.out.println(pattern1.matcher("010-12345678").matches()); // true
        System.out.println(pattern1.matcher("021-123456").matches()); // true
        System.out.println(pattern1.matcher("022#1234567").matches()); // false
        //使用Matcher时，必须首先调用matches()判断是否匹配成功，匹配成功后，才能调用group()提取子串。
        // 获得Matcher对象:
        Matcher matcher1 = pattern1.matcher("010-12345678");
        if (matcher1.matches()) {
            String whole = matcher1.group(0); // "010-12345678", 0表示匹配的整个字符串
            String area = matcher1.group(1); // "010", 1表示匹配的第1个子串
            String tel = matcher1.group(2); // "12345678", 2表示匹配的第2个子串
            System.out.println(area);
            System.out.println(tel);
        }

        /**给定一个字符串表示的数字，判断该数字末尾0的个数
         * 然而，正则表达式默认使用 贪婪匹配 ：任何一个规则，它总是尽可能多地向后匹配，因此，\d+总是会把后面的0包含进来。
         */
        Pattern pattern2 = Pattern.compile("(\\d+)(0*)");
        Matcher matcher2 = pattern2.matcher("1230000");
        if (matcher2.matches()) {
            System.out.println("group1=" + matcher2.group(1)); // "1230000"
            System.out.println("group2=" + matcher2.group(2)); // ""
        }

        /**要让\d+尽量少匹配，让0*尽量多匹配，我们就必须让\d+使用非贪婪匹配。
         * 在规则\d+后面 加个? 即可表示 非贪婪匹配。
         */
        Pattern pattern3 = Pattern.compile("(\\d+?)(0*)");
        Matcher matcher3 = pattern3.matcher("1230000");
        if (matcher3.matches()) {
            System.out.println("group1=" + matcher3.group(1)); // "123"
            System.out.println("group2=" + matcher3.group(2)); // "0000"
        }

        //使用正则表达式分割字符串可以实现更加灵活的功能。String.split()方法传入的正是正则表达式。
        //使用合适的正则表达式，就可以消除多个空格、混合,和;这些不规范的输入，直接提取出规范的字符串。
        System.out.println(Arrays.toString("a b c".split("\\s"))); // { "a", "b", "c" }
        System.out.println(Arrays.toString("a b  c".split("\\s"))); // { "a", "b", " ", "c" }
        System.out.println(Arrays.toString("a, b ;; c".split("[\\,\\;\\s]+"))); // { "a", "b", "c" }

        //使用正则表达式还可以搜索字符串
        //我们获取到Matcher对象后，不需要调用matches()方法（因为匹配整个串肯定返回false），
        // 而是反复调用find()方法，在整个串中搜索能匹配上\\w+o\\w规则的子串，并打印出来
        String s1 = "the quick brown fox jumps over the lazy dog.";
        Pattern p = Pattern.compile("\\w+o\\w");
        Matcher m = p.matcher(s1);
        while (m.find()) {  //循环查找
            //从s的[ m.start(), m.end() )区间截取的子串即为匹配的
            String sub = s1.substring(m.start(), m.end());
            System.out.println(m.start()+"--"+m.end()+":"+sub);
        }

        //使用正则表达式替换字符串可以直接调用String.replaceAll()，它的第一个参数是正则表达式，第二个参数是待替换的字符串
        String s2 = "The     quick\t\t brown   fox  jumps   over the  lazy dog.";
        String r1 = s2.replaceAll("\\s+", " ");
        System.out.println(r1); // "The quick brown fox jumps over the lazy dog."

        //要把搜索到的指定字符串按规则替换，比如前后各加一个<b>xxxx</b>，
        // 这个时候，使用replaceAll()的时候，
        // 我们传入的第二个参数可以使用 $1、$2 来 反向引用 匹配到的子串,
        // 即替换该分组，从1开始计数。未替换的分组被丢弃。
        String s3 = "the quick brown fox jumps over the lazy dog.";
        String r2 = s3.replaceAll("\\s([a-z]{4})\\s", " <b>$1</b> ");
        System.out.println(r2);
        String r3 = s3.replaceAll("\\s(\\w)([a-z]{4})\\s", " <b>$1</b>$2 ");
        System.out.println(r3);
        String r4 = s3.replaceAll("\\s(\\w)([a-z]{4})\\s", " <b>$1</b> ");
        System.out.println(r4);


        //将字符串中的cat换成dog，并将替换完的字符串保存到sb中
        Pattern pp = Pattern.compile("cat");
        Matcher mm = pp.matcher("one cat two cats in the yard");
        StringBuilder sb = new StringBuilder();
        while (mm.find()) {
            //public Matcher appendReplacement​(StringBuilder sb,String replacement)
            //sb-目标字符串构建器，replacement-替换字符串
            mm.appendReplacement(sb, "dog");
            System.out.println(sb.toString());  //one dog --> one dog two dog
        }
        //添加剩余部分到sb
        mm.appendTail(sb);
        System.out.println(sb.toString());      //one dog two dogs in the yard


        //模板引擎是指，定义一个字符串作为模板
        //以${key}表示的是变量，也就是将要被替换的内容
        //当传入一个Map<String, String>给模板后，需要把对应的key替换为Map的value。

        //简单的模板引擎，利用正则表达式实现这个功能
        Template t = new Template("Hello, ${name}! You are learning ${lang}!");
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Bob");
        data.put("lang", "Java");
        System.out.println("Hello, Bob! You are learning Java!".equals(t.render(data)));

    }

}


class Template {

    final String template;
    final Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}");     //group(1)即为key

    public Template(String template) {
        this.template = template;
    }

    public String render(Map<String, Object> data) {
        Matcher m = pattern.matcher(template);

        StringBuilder sb=new StringBuilder();
        while (m.find()){
            m.appendReplacement(sb, (String) data.get(m.group(1)));
        }
        m.appendTail(sb);

        return sb.toString();
    }

}