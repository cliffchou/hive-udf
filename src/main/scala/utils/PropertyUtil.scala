package utils

import java.util
import java.util.Properties

import com.ctrip.framework.apollo.{Config, ConfigService}
import com.google.common.collect.Sets
import org.apache.commons.collections.CollectionUtils
import org.apache.commons.lang.StringUtils

import scala.collection.JavaConversions._

/**
 * Created by yunhui li on 2018/6/25.
 */
object PropertyUtil {

  private val paths: util.Set[String] = Sets.newHashSet()
  private val confProperties: Properties = new Properties

  if (CollectionUtils.isNotEmpty(paths)) {
    for (path: String <- paths) {
      load(path);
    }
  }


  def load(path: String) {

    ////使用测试Apollo配置
    /*System.setProperty("app.id","95c2c73121f1e97379a2be0a6ea295f1");
    System.setProperty("env","PRO");
    System.setProperty("apollo.cluster","rec_test");*/

    //线上Apollo
    /*System.setProperty("app.id","320d8436e582816b272e4ca009460973");
    System.setProperty("env","PRO");
    System.setProperty("apollo.cluster","sohutv-task-online");*/

    if (StringUtils.isEmpty(path) || paths.contains(path)) {
      return
    }
    val config: Config = ConfigService.getConfig(path)
    val names = config.getPropertyNames
    import scala.collection.JavaConversions._
    for (name <- names) {
      confProperties.put(name, config.getProperty(name, ""))
    }
    paths.add(path)
  }

  def getProperty(key: String, filePath: String): String = {
    if (StringUtils.isEmpty(key)) {
      return null
    }
    if (!StringUtils.isEmpty(filePath) && !paths.contains(filePath)) {
      load(filePath)
    }
    return if (confProperties.containsKey(key)) confProperties.get(key).asInstanceOf[String]
    else null
  }

  def getProperty(key: String): String = {
    return if (confProperties.containsKey(key)) confProperties.get(key).asInstanceOf[String]
    else null
  }

  def getAllProperties(filePath: String): Properties = {
    if (!StringUtils.isEmpty(filePath) && !paths.contains(filePath)) {
      load(filePath)
    }
    return confProperties
  }

}