package com.ehangwork.tool.channel

import picocli.CommandLine

import java.util.concurrent.Callable

@CommandLine.Command(description = "查看渠道号工具",
        name = "show", mixinStandardHelpOptions = true, version = "v2.0.0")
class ShowChannel implements Callable<Void> {

    @CommandLine.Parameters(index = "0", description = "Apk包目录地址.")
    private File apkFilePath

    @Override
    Void call() throws Exception {
        if (!apkFilePath.exists()) {
            println apkFilePath.absolutePath
            println '指定Apk文件不存在!!!'
            System.exit(-1)
        }

        String channelV1 = ChannelUtil.getChannelV1(apkFilePath)
        String channelV2 = ChannelUtil.getChannelV2(apkFilePath)
        if (channelV1) {
            println "读取V1渠道号- $channelV1"
        }

        if (channelV2) {
            println "读取V2渠道号- $channelV2"
        }

    }

}
