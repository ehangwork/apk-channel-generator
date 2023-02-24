package com.ehangwork.tool.channel

import org.apache.commons.io.FileUtils
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import picocli.CommandLine

import java.util.concurrent.Callable

@CommandLine.Command(description = "Android签名版本渠道生成工具",
        name = "channel-generator", mixinStandardHelpOptions = true, version = "v2.0.0")
class ChannelGenerator implements Callable<Void> {

    @CommandLine.Parameters(index = "0", description = "打包生成的Apk包目录地址.")
    private File apkFilePath
    @CommandLine.Parameters(index = "1", description = "指定渠道号文件.")
    private File channelsFile

    @CommandLine.Option(names = ["-a", "--appendChannel"], description = "指定需要额外添加的渠道号")
    private String appendChannels
    @CommandLine.Option(names = ["--apkFileName"], description = "指定使用的Apk包名称")
    private String apkFileName
    @CommandLine.Option(names = ["-o", "--output"], description = "指定输出目录")
    private File outputFile

    private List<Map<String, String>> channels = new ArrayList<>()

    @Override
    Void call() throws Exception {

        if (!apkFilePath.exists()) {
            println '指定Apk目录不存在!!!'
            System.exit(-1)
        }

        if (!outputFile) {
            outputFile = new File(apkFilePath, "output")
        }

//        if (outputFile.exists() && outputFile.listFiles()) {
//            println "输出目录不是空目录，请检查!!! [${outputFile.absolutePath}]"
//            System.exit(-1)
//        }

        println "开始读取渠道信息"
        channels.addAll(readChannelFromFile())
//        if (appendChannels) {
//            String[] addChannels = appendChannels.split(",")
//            channels.addAll(addChannels as Map<String, String>[])
//        }

        println "总共渠道数，${channels.size()}"

        if (apkFileName) {
            generator()
        } else {
            println "请指定apk包名称"
        }

        println "渠道包生成完成!!!\n开始校验..."

        validateChannelApk(outputFile)

        if (mErrorList) {
            mErrorList.forEach() {
                println it
            }
            System.exit(-1)
        } else {
            println "渠道包校验成功"
        }

        return null
    }

    private List<Map<String, String>> readChannelFromFile() {
        List<Map<String, String>> readResult = new ArrayList<>()
        try {
            List<List<String>> result = readXlsx(channelsFile)
            for (int i = 0; i < result.size(); i++) {
                List<String> model = result.get(i)
                Map<String, String> map = new HashMap<>()
                map.put("channel", model.get(0))
                if (model.size() >= 2 && !model.get(1).isEmpty()) {
                    map.put("extraInfo", model.get(1))
                }
                readResult.add(map)
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        return readResult
    }

    private void generator() {
        channels.forEach() {
            println "正在生成渠道-$it"
            File outputApkFile = new File(outputFile, it.get("channel") + ".apk")
            outputApkFile.parentFile.mkdirs()
            FileUtils.copyFile(new File(apkFilePath, apkFileName), outputApkFile)
            addChannel(outputApkFile, it)
        }
    }

    private List<String> mErrorList = []

    private void validateChannelApk(File outputFile) {
        boolean validate = true
        outputFile.listFiles().each {
            String realChannel = getChannel(it)
            if(it.name.indexOf(".apk") > -1) {
                String channel = it.name.substring(0, it.name.indexOf(".apk"))
                if (channel != realChannel) {
                    validate = false
                    println "渠道包不正确，目标渠道为${it.name}，实际渠道为${realChannel}, ${it.absolutePath}"
                }
            }
        }

        if (validate) {
            println "渠道包校验完成-成功"
        } else {
            println "渠道包校验完成-失败"
            mErrorList.add("渠道包校验完成-失败")
        }
    }

    private static void addChannel(File apkFile, Map<String, String> channel) {
        if (channel.containsKey("extraInfo")) {
            Map<String, String> extraInfo = new HashMap<>()
            extraInfo.put("extraInfo", channel.get("extraInfo"))
            ChannelUtil.writeChannelV2(apkFile, channel.get("channel"), extraInfo)
        } else {
            ChannelUtil.writeChannelV2(apkFile, channel.get("channel"))
        }
    }

    private static String getChannel(File apkFile) {
        ChannelUtil.getChannelV2(apkFile)
    }

    private static List<List<String>> readXlsx(File path) throws Exception {
        InputStream is = new FileInputStream(path)
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is)
        List<List<String>> result = new ArrayList<List<String>>()
        // 循环每一页，并处理当前循环页
        for (XSSFSheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null) {
                continue
            }
            // 处理当前页，循环读取每一行
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum)
                int minColIx = xssfRow.getFirstCellNum()
                int maxColIx = xssfRow.getLastCellNum()
                List<String> rowList = new ArrayList<String>()
                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                    XSSFCell cell = xssfRow.getCell(colIx);
                    if (cell == null) {
                        continue
                    }
                    rowList.add(cell.toString())
                }
                result.add(rowList)
            }
        }
        return result
    }
}
