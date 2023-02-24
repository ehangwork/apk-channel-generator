package com.ehangwork.tool.channel

import com.meituan.android.walle.ChannelInfo
import com.meituan.android.walle.ChannelReader
import com.meituan.android.walle.ChannelWriter
import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.model.FileHeader
import net.lingala.zip4j.model.ZipParameters

class ChannelUtil {

    static void writeChannelV1(File apkFile, String channel) {
        ZipFile zipFile = new ZipFile(apkFile)
        File file = new File("cztchannel_$channel")
        if (!file.exists()) {
            file.createNewFile()
        }

        ZipParameters zipParameters = new ZipParameters()
        zipParameters.setRootFolderInZip("META-INF")
        zipFile.addFile(file, zipParameters)

        file.delete()
    }

    static void writeChannelV2(File apkFile, String channel) {
        ChannelWriter.put(apkFile, channel, false)
    }

    static void writeChannelV2(File apkFile, String channel, Map<String, String> extraInfo) {
        ChannelWriter.put(apkFile, channel, extraInfo, false)
    }

    static String getChannelV1(File apkFile) {
        String channelFlag = "cztchannel_"
        ZipFile zipFile = new ZipFile(apkFile)
        List<FileHeader> fileHeaderList = zipFile.getFileHeaders() as List<FileHeader>
        for (FileHeader fileHeader : fileHeaderList) {
            if (fileHeader.fileName.contains(channelFlag)) {
                return fileHeader.fileName.substring(fileHeader.fileName.indexOf(channelFlag) + channelFlag.length())
            }
        }
        return ''
    }

    static String getChannelV2(File apkFile) {
        ChannelInfo channelInfo = ChannelReader.get(apkFile)
        if(channelInfo != null){
            return channelInfo.channel
        }
        return ''
    }

}
