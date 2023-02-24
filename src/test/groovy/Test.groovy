import com.ehangwork.tool.channel.ChannelTool
import picocli.CommandLine

class Test {

    static void main(String[] args) {

//        def cmd = "generator G:\\Workspace\\code\\yxlife\\yxlife-feature\\main\\build\\outputs\\apk\\release 1.0.0 -c 0-10,20-30 -a 100 --apkFileName main-release.apk -s v2"
        def cmd = "zgs-generator /Users/ehang/temp/1.0.0 1.0.0 /Users/ehang/temp/默认渠道包.xlsx -o /Users/ehang/temp/channel --apkFileName app-release.apk"
//        def cmd = "yx-generator e:\\Desktop\\1.0.0 1.0.0 -c 1-10 --appendChannel 21,22 --apkFileName main-release.apk"
//
        args = cmd.split(" ")

        CommandLine.call(new ChannelTool(), args)
    }
}
