package utils

import grizzled.slf4j.Logger

//在不指定参数情况下，默认载入resources/application.conf的配置
//若存在指定参数，则依照指定参数路径下的配置文件
//所有配置项没有则添加，有相同key的情况下参照后者

object ConfigLoader {
  @transient lazy implicit val logger: Logger = Logger[this.type]


  def brokers: String = PropertyUtil.getProperty("brokers",
    "kafka.properties").toString

  def sofaTopics: String = PropertyUtil.getProperty("sofa_topics",
    "kafka.properties").toString

  def sofaTopicsServer: String = PropertyUtil.getProperty("sofa_topics_server",
    "kafka.properties").toString

  def topicsVideo: String = PropertyUtil.getProperty("topics_video",
    "kafka.properties").toString

  def consumerGroup: String = PropertyUtil.getProperty("consumer_group",
    "kafka.properties").toString

  def startOffset: String = PropertyUtil.getProperty("startOffset",
    "kafka.properties").toString

  def authorKafkaBrokerList = PropertyUtil.getProperty("author_brokers",
    "kafka.properties").toString

  def authorKafkaTopic = PropertyUtil.getProperty("author_topic",
    "kafka.properties").toString

  def authorKafkaGroup = PropertyUtil.getProperty("author_group",
    "kafka.properties").toString

  def authorKafkaPollTimeout = PropertyUtil.getProperty("author_poll_timeout",
    "kafka.properties").toLong

  def batchInterval: Int = PropertyUtil.getProperty("batchinterval", "spark.properties").toInt

  def repartionNum: Int = PropertyUtil.getProperty("repartionNum", "spark.properties").toInt

  def mysqlShortVideo: String = PropertyUtil.getProperty("table.shortVideo", "mysql.properties")

  def mysqlSmallVideo: String = PropertyUtil.getProperty("table.smallVideo", "mysql.properties")

  def mysqlTaglib: String = PropertyUtil.getProperty("table.taglib", "mysql.properties")

  def eventWeekTable: String = PropertyUtil.getProperty("eventWeekTable", "hbase.properties")

  def serverNamespaceTable: String = PropertyUtil.getProperty("serverNamespaceTable", "hbase.properties")

  def eventStatisticsTable: String = PropertyUtil.getProperty("eventStatisticsTable", "hbase.properties")

  def shortVideoTable: String = PropertyUtil.getProperty("shortVideoTable", "hbase.properties")

  def smallVideoTable: String = PropertyUtil.getProperty("smallVideoTable", "hbase.properties")

  def authorMappingTable: String = PropertyUtil.getProperty("authorMappingTable", "hbase.properties")

  def taglibTable: String = "TODO"

  val eventLogDir = PropertyUtil.getProperty("eventLogDir", "dir.properties").toString


  //以下均为添加appolo配置,所有新增配置均使用阿波罗配置中心
  def mysqlUsernameApollo: String = PropertyUtil.getProperty("username", "mysql.properties")

  def mysqlPasswordApollo: String = PropertyUtil.getProperty("password", "mysql.properties")

  def mysqlUrlApollo: String = PropertyUtil.getProperty("url", "mysql.properties")

  def mysqlJdbcApollo: String = PropertyUtil.getProperty("jdbc", "mysql.properties")

  def mysqlDbApollo: String = PropertyUtil.getProperty("database", "mysql.properties")

  def mysqlHotWordTableApollo: String = PropertyUtil.getProperty("table.hotword", "mysql.properties")

  def mysqlChannelTableApollo: String = PropertyUtil.getProperty("table.channel",
    "mysql.properties")

  def profileNegativeFactor: Double = PropertyUtil.getProperty("userprofile.negativeFactor", "EventUserProfileOnline.properties").toDouble

  def profilePositiveFactor: Double = PropertyUtil.getProperty("userprofile.positiveFactor", "EventUserProfileOnline.properties").toDouble


  object UserChannelPreferenceProfile {
    val userPreferenceProfilePathApollo: String = PropertyUtil.getProperty("dir" +
      ".userPreferenceProfile",
      "userChannelPreference.properties")

    val userPreferenceEventTimeRangeApollo: Int =
      PropertyUtil.getProperty("duration.userPreferenceEventTimeRange", "userChannelPreference.properties").toInt

    val channelPreferenceShowWight: Double = PropertyUtil.getProperty("weight.show",
      "userChannelPreference.properties").toDouble

    val channelPreferenceViewFasleWight: Double = PropertyUtil.getProperty("weight.view.false",
      "userChannelPreference.properties").toDouble

    val channelPreferenceViewTrueWight: Double = PropertyUtil.getProperty("weight.view.true",
      "userChannelPreference.properties").toDouble

    val channelPreferenceShareWight: Double = PropertyUtil.getProperty("weight.share",
      "userChannelPreference.properties").toDouble

    val channelPreferenceLikeWight: Double = PropertyUtil.getProperty("weight.like",
      "userChannelPreference.properties").toDouble

    val channelPreferenceCollectWight: Double = PropertyUtil.getProperty("weight.collect",
      "userChannelPreference.properties").toDouble

    val showViewFactor: Double = PropertyUtil.getProperty("weight.showViewFactor",
      "userChannelPreference.properties").toDouble

    val showCountFactor: Double = PropertyUtil.getProperty("weight.showCountFactor",
      "userChannelPreference.properties").toDouble

  }

  object UserVideoBaseFeature {
    val userFeatureEventTimeRange: Int = PropertyUtil.getProperty("duration" +
      ".userFeatureEventTimeRange", "userVideoBaseFeature.properties").toInt
  }

  object NewVideoSetOffline {
    val durationOtherChannel = PropertyUtil.getProperty("duration.otherchannel",
      "NewVideoSetOffline.properties").toInt
    val durationMainChannel = PropertyUtil.getProperty("duration.mainchannel",
      "NewVideoSetOffline.properties").toInt
    val timeDecayFactor = PropertyUtil.getProperty("timeDecayFactor",
      "NewVideoSetOffline.properties").toDouble
  }

  object HighCtrCandidateProp {
    // 输入目录前缀
    val inputBase = PropertyUtil.getProperty("highctr.input.base",
      "Highctr.properties").toString

    // 输出目录
    val outputBase = PropertyUtil.getProperty("highctr.output.base",
      "Highctr.properties").toString

    // 曝光限制
    val expoLimit = PropertyUtil.getProperty("highctr.expo.limit",
      "Highctr.properties").toInt

    // 点击率限制
    val rateLimit = PropertyUtil.getProperty("highctr.rate.limit",
      "Highctr.properties").toDouble

    // 起始天数限制
    val dayStartLimit = PropertyUtil.getProperty("highctr.day.start.limit",
      "Highctr.properties").toInt

    // 上传时间多少天限制
    val uploadDayLimit = PropertyUtil.getProperty("highctr.day.upload.limit",
      "Highctr.properties").toInt

    // 中间状态Key eg: video:highctr:VID
    val stateKeyPrifix = PropertyUtil.getProperty("highctr.state.key.prefix",
      "Highctr.properties").toString

    // 中间状态field playCount
    val playCountField = PropertyUtil.getProperty("highctr.state.field.playCount",
      "Highctr.properties").toString

    // 中间状态field showCount
    val showCountField = PropertyUtil.getProperty("highctr.state.field.showCount",
      "Highctr.properties").toString

    // 无频道key  eg: highctr:nochannel
    val nochannelKey = PropertyUtil.getProperty("highctr.nochannel.key",
      "Highctr.properties").toString

    // 频道key前缀 eg: highctr:channel:CATEGORY
    val channelKeyPrifix = PropertyUtil.getProperty("highctr.channel.key.prefix",
      "Highctr.properties").toString
  }

  object CtrCandidateProp {
    // 输入目录前缀
    val inputBase = PropertyUtil.getProperty("ctr.input.base",
      "Ctr.properties").toString

    // 输出目录
    val outputBase = PropertyUtil.getProperty("ctr.output.base",
      "Ctr.properties").toString

    // 精品曝光限制
    val expoTop = PropertyUtil.getProperty("ctr.expo.top",
      "Ctr.properties").toInt

    // 精品点击率限制
    val rateTop = PropertyUtil.getProperty("ctr.rate.top",
      "Ctr.properties").toDouble

    // 曝光限制
    val expoMiddle = PropertyUtil.getProperty("ctr.expo.middle",
      "Ctr.properties").toInt

    // 点击率限制
    val rateMiddle = PropertyUtil.getProperty("ctr.rate.middle",
      "Ctr.properties").toDouble

    // 起始天数限制
    val dayStartLimit = PropertyUtil.getProperty("ctr.day.start.limit",
      "Ctr.properties").toInt

    // 上传时间多少天限制
    val uploadDayLimit = PropertyUtil.getProperty("ctr.day.upload.limit",
      "Ctr.properties").toInt

    // 中间状态Key eg: video:ctr:VID
    val stateKeyPrifix = PropertyUtil.getProperty("ctr.state.key.prefix",
      "Ctr.properties").toString

    // 中间状态field playCount
    val playCountField = PropertyUtil.getProperty("ctr.state.field.playCount",
      "Ctr.properties").toString

    // 中间状态field showCount
    val showCountField = PropertyUtil.getProperty("ctr.state.field.showCount",
      "Ctr.properties").toString

    // 精品无频道key  eg: ctr:top:nochannel
    val topNochannelKey = PropertyUtil.getProperty("ctr.top.nochannel.key",
      "Ctr.properties").toString

    // 精品频道key前缀 eg: ctr:top:channel:CATEGORY
    val topChannelKeyPrifix = PropertyUtil.getProperty("ctr.top.channel.key.prefix",
      "Ctr.properties").toString

    // 无频道key  eg: ctr:middle:nochannel
    val middleNochannelKey = PropertyUtil.getProperty("ctr.middle.nochannel.key",
      "Ctr.properties").toString

    // 频道key前缀 eg: ctr:middle:channel:CATEGORY
    val middleChannelKeyPrifix = PropertyUtil.getProperty("ctr.middle.channel.key.prefix",
      "Ctr.properties").toString

  }

  object GreatChoiceProp {
    // 输出目录
    val outputBase = PropertyUtil.getProperty("choice.output.base",
      "GreatChoice.properties").toString

    // 用mTime限制的小时数
    val mTimeLimit = PropertyUtil.getProperty("choice.mtime.limit",
      "GreatChoice.properties").toInt

    // nochannelKey  choice:nochannel
    val nochannelKey = PropertyUtil.getProperty("choice.nochannel.key",
      "GreatChoice.properties").toString

    // channelKeyPrifix  choice:channel:
    val channelPrifix = PropertyUtil.getProperty("choice.channel.prefix",
      "GreatChoice.properties").toString

    // Key过期时间  86400s
    val keyExpire = PropertyUtil.getProperty("choice.key.expire",
      "GreatChoice.properties").toInt
  }

  object BlmCheckProp {
    // 输入目录
    val inputBase = PropertyUtil.getProperty("blmcheck.input.base",
      "BlmCheck.properties").toString

    // 输出目录
    val outputBase = PropertyUtil.getProperty("blmcheck.output.base",
      "BlmCheck.properties").toString

    // 多少个以上的做处理
    val limit = PropertyUtil.getProperty("blmcheck.num.limit",
      "BlmCheck.properties").toInt

    // 从多少天前开始的推荐日志
    val recDayStart = PropertyUtil.getProperty("blmcheck.rec.daystart",
      "BlmCheck.properties").toInt

    // 从多少天前开始的曝光日志
    val expoStart = PropertyUtil.getProperty("blmcheck.expo.daystart",
      "BlmCheck.properties").toInt

  }

  object FeatureCompute {

    val userBasicFeaturePath = PropertyUtil.getProperty("baseFeature.input.path",
      "FeatureCompute.properties").toString
    val videoFeaturePath = PropertyUtil.getProperty("videoFeature.input.path",
      "FeatureCompute.properties").toString
    val userBehaviorFeaturePath = PropertyUtil.getProperty("behaviorFeature.input.path",
      "FeatureCompute.properties").toString
    val testFeaturePath = PropertyUtil.getProperty("testFeature.input.path",
      "FeatureCompute.properties").toString
    val trainingFeaturePath = PropertyUtil.getProperty("trainingFeature.input.path",
      "FeatureCompute.properties").toString
    val trainingFeatureDuration = PropertyUtil.getProperty("duration.trainingFeature",
      "FeatureCompute.properties").toInt
    val testFeatureDuration = PropertyUtil.getProperty("duration.testFeature",
      "FeatureCompute.properties").toInt

    val behaviorFeatureDuration = PropertyUtil.getProperty("duration.behaviorFeature",
      "FeatureCompute.properties").toInt

    val userInterestFeaturePath = PropertyUtil.getProperty("userInterestTagFeature.input.path",
      "FeatureCompute.properties").toString
  }

  object modelPath {
    val word2vector = PropertyUtil.getProperty("word2vector", "modelPath.properties").toString
  }

  object VideoFeatureOnline {
    val videoFeatureKey = PropertyUtil.getProperty("video.feature.key",
      "VideoFeatureOnline.properties").toString
    val ner_model_path = PropertyUtil.getProperty("path.ner_model_path",
      "VideoFeatureOnline.properties").toString
    val ner_dic_path = PropertyUtil.getProperty("path.ner_dic_path",
      "VideoFeatureOnline.properties").toString
    val stop_word_path = PropertyUtil.getProperty("path.stop_word_path",
      "VideoFeatureOnline.properties").toString
    val sogou_related_count = PropertyUtil.getProperty("sogou_related_count",
      "VideoFeatureOnline.properties").toDouble
    val baidu_related_count = PropertyUtil.getProperty("baidu_related_count",
      "VideoFeatureOnline.properties").toDouble

  }

  object EventUserProfileOnline {
    val userprofileCheckPoint = PropertyUtil.getProperty("userprofileCheckPoint",
      "EventUserProfileOnline.properties").toString
    val userProfileDuration = PropertyUtil.getProperty("userProfileOnlineBatchTime",
      "EventUserProfileOnline.properties").toInt

    def shortActor: Double = PropertyUtil.getProperty("shortVideoTagWeight.actor",
      "EventUserProfileOnline.properties").toDouble

    def shortArea: Double = PropertyUtil.getProperty("shortVideoTagWeight.area",
      "EventUserProfileOnline.properties").toDouble

    def shortCategory: Double = PropertyUtil.getProperty("shortVideoTagWeight.category",
      "EventUserProfileOnline.properties").toDouble

    def shortDate: Double = PropertyUtil.getProperty("shortVideoTagWeight.date",
      "EventUserProfileOnline.properties").toDouble

    def shortOther: Double = PropertyUtil.getProperty("shortVideoTagWeight.other",
      "EventUserProfileOnline.properties").toDouble

    def shortType: Double = PropertyUtil.getProperty("shortVideoTagWeight.type",
      "EventUserProfileOnline.properties").toDouble

  }

  object EventProcessOnline {
    val vidPopSetSize = PropertyUtil.getProperty("vidPopSetSize",
      "EventProcessOnline.properties").toInt

    val siteSortInfo = PropertyUtil.getProperty("siteSortInfo",
      "EventProcessOnline.properties").toString

    val profileShareTimesOfPlay = PropertyUtil.getProperty("profileShareTimesOfPlay",
      "EventProcessOnline.properties").toInt
    val profileDislikeTimesOfPlay = PropertyUtil.getProperty("profileDislikeTimesOfPlay",
      "EventProcessOnline.properties").toInt

    val vidPopOnLinePlayShrik = PropertyUtil.getProperty("vidPopOnLinePlayShrik",
      "EventProcessOnline.properties").toDouble
    val vidPlayWeight = PropertyUtil.getProperty("vidPlayWeight",
      "EventProcessOnline.properties").toDouble
    val vidPopOnLineShareShrik = PropertyUtil.getProperty("vidPopOnLineShareShrik",
      "EventProcessOnline.properties").toDouble
    val vidShareWeight = PropertyUtil.getProperty("vidShareWeight",
      "EventProcessOnline.properties").toDouble
    val vidPopOnLineLikeShrik = PropertyUtil.getProperty("vidPopOnLineLikeShrik",
      "EventProcessOnline.properties").toDouble
    val vidLikeWeight = PropertyUtil.getProperty("vidLikeWeight",
      "EventProcessOnline.properties").toDouble

    val vidPopOnLineCollectShrik = PropertyUtil.getProperty("vidPopOnLineCollectShrik",
      "EventProcessOnline.properties").toDouble
    val vidCollectWeight = PropertyUtil.getProperty("vidCollectWeight",
      "EventProcessOnline.properties").toDouble
    val vidPopExtendCoe = PropertyUtil.getProperty("vidPopExtendCoe",
      "EventProcessOnline.properties").toDouble
    val vidPopExtendBeta = PropertyUtil.getProperty("vidPopExtendBeta",
      "EventProcessOnline.properties").toDouble

  }

  object userProfile {
    val  videoInfokeyPreffix = PropertyUtil.getProperty("videoInfokeyPreffix", "UserProfile" +
      ".properties").toString
    val profilePathPreffix = PropertyUtil.getProperty("profilePathPreffix", "UserProfile" +
      ".properties").toString
    val eventDataGap = PropertyUtil.getProperty("eventDataGap","UserProfile.properties").toInt

    val basicProfilePath = PropertyUtil.getProperty("dir.userBasicProfile","UserProfile.properties").toString

    val behaviorProfilePath = PropertyUtil.getProperty("dir.userBehaviorProfile","UserProfile.properties").toString

    val baseProfileBaseTime = PropertyUtil.getProperty("duration.userProfileBaseTime",
      "UserProfile.properties").toInt

    val behaviorProfileBaseTime = PropertyUtil.getProperty("duration.userBehaviorProfileBaseTime",
      "UserProfile.properties").toInt

  }


  object DataPath {
    val topicLog = PropertyUtil.getProperty("topicLog", "DataPath.properties")
    val topicOutPath = PropertyUtil.getProperty("topicOutpath", "DataPath.properties")
    val pbLog = PropertyUtil.getProperty("pbLog", "DataPath.properties")
    val modelDataPath = PropertyUtil.getProperty("modelDataPath", "DataPath.properties")
  }

}
