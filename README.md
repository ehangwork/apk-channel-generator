
# apk-channel-generator

Jenkins 构建Android渠道包脚本程序！

### 快速开始

1. 下载tool下的工具包并解压缩
2. 配置Path，指定bin目录即可在任何目录调用命令channel-tool
3. 支持子命令有 show, channel-generator

Jenkins工程配置下添加Execute shell，执行以下脚本
```js
publishApkPath = "/.jenkins/apks" //源APK位置路径
channelsFile="/.jenkins/apks/channel.xlsx" //渠道文件, 这里使用excel文件
outputPath="/.jenkins/apks/channels"//生成渠道包存放目录
appName="app-release.apk" //app文件名称

channel-tool channel-generator $publishApkPath $channelsFile -o $outputPath --apkFileName $appName
```

#### 渠道号读取使用[美团walle渠道包打包神器](https://github.com/Meituan-Dianping/walle)文档方式即可！

## 参考
* [美团walle渠道包打包神器](https://github.com/Meituan-Dianping/walle)

### License

Copyright 2020 ehangWork

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

