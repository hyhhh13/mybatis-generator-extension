package com.github.trang.mybaits.generator.extension;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;

import java.util.List;
import java.util.Properties;

/**
 * 自定义MBG Mapping插件
 * 完善通用Mapper生成的Mapping文件，加入Base_Columns属性
 *
 * @author trang
 */
public class MapperSqlMapConfigPlugin extends PluginAdapter {

    public MapperSqlMapConfigPlugin() {
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 生成Mapping文件中的Element
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        generateSqlBaseColumns(document, introspectedTable);
        return true;
    }

    /**
     * 生成包含全部列的sql元素
     *
     * @param document
     * @param introspectedTable
     */
    private void generateSqlBaseColumns(Document document, IntrospectedTable introspectedTable) {
        //获取根元素
        XmlElement rootElement = document.getRootElement();
        //新建sql元素标签
        XmlElement sqlElement = new XmlElement("sql");
        //新建sql元素属性
        Attribute attr = new Attribute("id", "BaseColumns");
        sqlElement.addAttribute(attr);
        //新建sql元素内容
        TextElement comment = new TextElement("<!-- WARNING - @mbg.generated -->");
        //获取全部列名称
        StringBuilder columnsBuilder = new StringBuilder();
        List<IntrospectedColumn> columnList = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : columnList) {
            columnsBuilder.append(column.getActualColumnName()).append(", ");
        }
        String columns = columnsBuilder.substring(0, columnsBuilder.length() - 2);
        TextElement content = new TextElement(columns);
        sqlElement.addElement(comment);
        sqlElement.addElement(content);
        //将sql元素放到根元素下
        rootElement.addElement(sqlElement);
    }
}
