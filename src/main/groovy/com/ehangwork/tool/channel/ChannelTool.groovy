package com.ehangwork.tool.channel

import picocli.CommandLine

import java.util.concurrent.Callable

@CommandLine.Command(description = "渠道生成工具",
        subcommands = [ShowChannel, ChannelGenerator],
        name = "channel-tool", mixinStandardHelpOptions = true, version = "v2.0.0")
class ChannelTool implements Callable<Void> {

    static void main(String[] args) {
        CommandLine.call(new ChannelTool(), args)

    }

    @Override
    Void call() throws Exception {
        return CommandLine.usage(this, System.out)
    }
}
