<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
          xmlns:o="urn:schemas-microsoft-com:office:office"
          xmlns:x="urn:schemas-microsoft-com:office:excel"
          xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
          xmlns:html="http://www.w3.org/TR/REC-html40">
    <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
        <Author>Microsoft Office User</Author>
        <LastAuthor>Microsoft Office User</LastAuthor>
        <Created>2020-11-15T08:36:40Z</Created>
        <LastSaved>2020-11-15T08:46:38Z</LastSaved>
        <Version>16.00</Version>
    </DocumentProperties>
    <OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
        <AllowPNG/>
    </OfficeDocumentSettings>
    <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
        <WindowHeight>16120</WindowHeight>
        <WindowWidth>28240</WindowWidth>
        <WindowTopX>280</WindowTopX>
        <WindowTopY>460</WindowTopY>
        <ActiveSheet>1</ActiveSheet>
        <ProtectStructure>False</ProtectStructure>
        <ProtectWindows>False</ProtectWindows>
    </ExcelWorkbook>
    <Styles>
        <Style ss:ID="Default" ss:Name="Normal">
            <Alignment ss:Vertical="Center"/>
            <Borders/>
            <Font ss:FontName="等线" x:CharSet="134" ss:Size="12" ss:Color="#000000"/>
            <Interior/>
            <NumberFormat/>
            <Protection/>
        </Style>
    </Styles>
    <#list tables><#items as t>
    <Worksheet ss:Name="${t.tableName!''}(${t.remarks!''})">
        <Table ss:ExpandedColumnCount="9" ss:ExpandedRowCount="${t.columns?size+6}" x:FullColumns="1"
               x:FullRows="1" ss:DefaultColumnWidth="65" ss:DefaultRowHeight="16">
            <Row>
                <Cell><PhoneticText xmlns="urn:schemas-microsoft-com:office:excel">xu</PhoneticText><Data
                            ss:Type="String">序号</Data></Cell>
                <Cell><Data ss:Type="String">名称</Data></Cell>
                <Cell><PhoneticText xmlns="urn:schemas-microsoft-com:office:excel">shu</PhoneticText><Data
                            ss:Type="String">数据类型</Data></Cell>
                <Cell><PhoneticText xmlns="urn:schemas-microsoft-com:office:excel">chang</PhoneticText><Data
                            ss:Type="String">长度</Data></Cell>
                <Cell><PhoneticText xmlns="urn:schemas-microsoft-com:office:excel">shu'z</PhoneticText><Data
                            ss:Type="String">小数值</Data></Cell>
                <Cell><PhoneticText xmlns="urn:schemas-microsoft-com:office:excel">yun</PhoneticText><Data
                            ss:Type="String">允许空值</Data></Cell>
                <Cell><PhoneticText xmlns="urn:schemas-microsoft-com:office:excel">zhu</PhoneticText><Data
                            ss:Type="String">主键</Data></Cell>
                <Cell><PhoneticText xmlns="urn:schemas-microsoft-com:office:excel">mo'ren</PhoneticText><Data
                            ss:Type="String">默认值</Data></Cell>
                <Cell><PhoneticText xmlns="urn:schemas-microsoft-com:office:excel">shuo</PhoneticText><Data
                            ss:Type="String">说明</Data></Cell>
            </Row>
            <#list t.columns>
            <#items as c>
            <Row>
                <Cell><Data ss:Type="Number">${c?index+1}</Data></Cell>
                <Cell><Data ss:Type="String">${c.columnName!''}</Data></Cell>
                <Cell><Data ss:Type="String">${c.typeName!''}</Data></Cell>
                <Cell><Data ss:Type="Number">${c.columnSize!''}</Data></Cell>
                <Cell><Data ss:Type="Number">${c.decimalDigits!'0'}</Data></Cell>
                <Cell><Data ss:Type="String"> ${c.nullable!''}</Data></Cell>
                <Cell><Data ss:Type="String">${c.primaryKey!''}</Data></Cell>
                <Cell><Data ss:Type="String"> ${c.columnDef!''}</Data></Cell>
                <Cell><Data ss:Type="String">${c.remarks!''}</Data></Cell>
            </Row>
            </#items>
            </#list>
        </Table>
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
            <PageSetup>
                <Header x:Margin="0.3"/>
                <Footer x:Margin="0.3"/>
                <PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7" x:Top="0.75"/>
            </PageSetup>
            <Selected/>
<#--            <Print>-->
<#--                <ValidPrinterInfo/>-->
<#--                <PaperSizeIndex>9</PaperSizeIndex>-->
<#--                <VerticalResolution>0</VerticalResolution>-->
<#--            </Print>-->
            <Panes>
                <Pane>
                    <Number>3</Number>
                    <ActiveRow>1</ActiveRow>
                </Pane>
            </Panes>
            <ProtectObjects>False</ProtectObjects>
            <ProtectScenarios>False</ProtectScenarios>
        </WorksheetOptions>
    </Worksheet>

    </#items>
    </#list>
</Workbook>
