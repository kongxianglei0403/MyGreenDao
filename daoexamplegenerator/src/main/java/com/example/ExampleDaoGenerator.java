package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class ExampleDaoGenerator {
    public static void main(String[] arg) throws Exception {

        //schema 该对象用来添加实体（entity）
        //两个参数  第一个表示数据库版本号、第二个自动生成代码的包路径
        Schema schema = new Schema(1, "com.atu.kxl.mygreendao.greendao");
//        当然，如果你愿意，你也可以分别指定生成的 Bean 与 DAO 类所在的目录，只要如下所示：
//        Schema schema1 = new Schema(1, "com.atu.kxl.mygreendao.bean");
//        schema.setDefaultJavaPackageDao("com.atu.kxl.mygreendao.dao");
// 模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
        // schema2.enableActiveEntitiesByDefault();
        // schema2.enableKeepSectionsByDefault();

        //一旦有了一个schema对象后，就可以使用它添加实体（entities）
        addNote(schema);
        new DaoGenerator().generateAll(schema,"D:/DownLoadDemo/MyGreenDao/app/src/main/java-gen");
    }

    private static void addNote(Schema schema) {
        //一个实体（类）关联了数据库中的一张表，该表的名字[Note](即类名)
        Entity note = schema.addEntity("Note");
        //重命名
//        note.setTableName("NOTE");
        //greendao会自动根据实体类的属性值来创建表字段，并赋予默认值
        //与Java中的驼峰是命名不同，数据库中的命名是用大写和下划线来分割单词
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }
}
