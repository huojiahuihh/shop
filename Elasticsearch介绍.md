##### Elasticsearch介绍

Elasticsearch是一个基于Apache Lucene(TM)的开源搜索引擎。Lucene是迄今为止最先进、性能、 功能 上来讲最全的搜索引擎库。 但是，Lucene只是一个库。想要使用它，你必须使用Java来作为开发 语言并将其直接集成到你的应用中，（以导入jar包的形式），Lucene非常复杂，你需要深入了解检索 的相关知识来理解它是如何工作的。 Elasticsearch也使用Java开发并使用Lucene作为其核心来实现所有索引和搜索的功能，是一个独立的 web项目。但是它的使用方式是通过简单的RESTful API来隐藏Lucene的复杂性，从而让全文搜索变得 简单。 Elasticsearch不仅仅是Lucene和全文搜索，我们还能这样去描述它： 分布式的实时(快!)文件存储，每个字段都被索引并可被搜索 分布式的实时分析搜索引擎 可以扩展到上百台服务器，处理PB级结构化或非结构化数据 

##### Elasticsearch特点

Elasticsearch具备以下特点： 分布式，无需人工搭建集群 Restful风格，一切API都遵循Rest原则，容易上手 近实时搜索，数据更新在Elasticsearch中几乎是完全同步的。 Elasticsearch 是一个分布式的搜索引擎，底层基于lucene,主要特点是可以完成全文检索， 支持海量数据pb级别，横向拓展，数据分片，等一系列功能、 良好的查询机制，支持模糊，区间，排序，分组，分页，等常规功能 横向可扩展性：只需要增加一台服务器，做一点儿配置，启动一下ES进程就可以并入集 分片机制提供更好的分布性：同一个索引分成多个分片（sharding），这点类似于HDFS的块机 制；分而治之的方式来提升处理效率，相信大家都不会陌生； 不足： 没有细致的权限管理机制,没有像MySQL那样的分各种用户，每个用户有不同的权限 单台节点部署的话，并发查询效率并不高， 使用场景： 爱奇艺搜电影，京东搜手机，qq搜好友，百度地址各种信息，嘀嘀打车，邮件搜索，微信还有，美团饭 店，旅游景点，可以说，搜索场景无处不在

##### Elasticsearch安装

安装es在linux中 ->在es的plugins目录中ik分词器

ik分词器 用于分词   !!!   列如 北京天安门 会被 分词为 北京 天安门  天安 北 京 天 安 门

plugins中安装的都是插件

##### 安装可视化工具kibana(是一款es可视化工具)

Kibana是一个基于Node.js的Elasticsearch索引库数据统计工具，可以利用Elasticsearch的聚合功能， 生成各种图表，如柱形图，线状图，饼图等。 而且还提供了操作Elasticsearch索引数据的控制台，并且提供了一定的API提示，非常有利于我们学习 Elasticsearch的语法。

Kibana依赖于node !!!!

#####  倒排索引

逻辑结构部分是一个倒排索引表： 

1、将要搜索的文档内容分词，所有不重复的词组成分词列表。

 2、将搜索的文档最终以Document方式存储起来。

 3、每个词和docment都有关联。

如下

![2021-03-11 (2)](C:\Users\Angelina\OneDrive\图片\屏幕快照\2021-03-11 (2).png)



##### 客户端API

Rest风格API

Elasticsearch提供了Rest风格的API，即http请求接口，而且也提供了各种语言的客户端API

官网:https://www.elastic.co/guide/en/elasticsearch/client/index.html



##### 操作索引

elasticsearch也是基于Lucene的全文检索库，本质也是存储数据，很多概念与MySQL类似的.

对比关系:

![屏幕截图 2021-03-11 210826](C:\Users\Angelina\Pictures\Saved Pictures\屏幕截图 2021-03-11 210826.png)

详细说明

上图列出了，数据库与es所有之间的所有名词对应关系，尤其注意一栏，index 在数据库当中是索 引的意思，设置后，可提高检索效率，但在es索引库当中everything is indexed 每一列默认都是 索引，突出了，es是搜索引擎效率快

![image-20210311211056975](C:\Users\Angelina\AppData\Roaming\Typora\typora-user-images\image-20210311211056975.png)

要注意的是：Elasticsearch本身就是分布式的，因此即便你只有一个节点，Elasticsearch默认也会对你 的数据进行分片和副本操作，当你向集群添加新数据时，数据也会在新加入的节点中进行平衡。



##### 创建索引(http请求)

##### 	语法

​	Elasticsearch采用Rest风格API，因此其API就是一次http请求，你可以用任何工具发起http请求

创建索引的请求格式：

请求方式:PUT  ,请求路径：/索引库名, 请求参数：json格式：

```json
{

 	"settings": {

 	"number_of_shards": 1,//分片数量

	 "number_of_replicas": 0 //副本

	} 

}
```

##### 使用kibana创建索引

```json
PUT /索引名称
```

number_of_shards 是数据分片数，如果只有一台机器，

设置为1 number_of_replicas 是数据备份数，如果只有一台机器，设置为0(不进行备份)

如果要设置分片 或 数据备份

```json
PUT /索引名称
{

 	"settings": {

 	"number_of_shards": 1,//分片数量  修改设置  分片

	 "number_of_replicas": 0 //副本  修改设置  副本
	
	} 

}
```

##### 查看索引设置

Get请求可以帮我们查看索引信息，格式：

```json
GET /索引库名

GET * //查询所有 索引库信息

```

##### 删除索引

删除索引使用DELETE请求

```json
DELETE /索引库名
```

 查看是否存在索引

​		

```json
HEAD /查看的索引名称
```

##### 创建mapping

​	语法

```json
PUT /student
{
	"mappings": {
		"properties": {
			"name":{
                "type": "text",
                "index": true,
                "store": true,
                "analyzer": "ik_max_word"
				},
        		"age":{
        			"type": "integer"
        		},
        		"birthday":{
                    "type": "date",
                    "format": "yyyy-MM-dd"
       				}
        		}
       		 },
            "settings": {
                "number_of_shards": 1,
                "number_of_replicas": 0
    		}
}
```

index : 该 index 选项控制是否对字段值建立索引。它接受 true 或 false ，默认为 true 。未索引的字段不可 查询。

store : 默认情况下，对字段值进行索引以使其可搜索，但不存储它们。这意味着可以查询该字段，但是无法检 索原始字段值。

analyzer: 指定分词器(我们使用的ik分词器)

##### 字段类型

![image-20210311212701943](C:\Users\Angelina\AppData\Roaming\Typora\typora-user-images\image-20210311212701943.png)

String类型，又分两种：

​		 text：可分词，不可参与聚合 

​		keyword：不可分词，数据会作为完整字段进行匹配，

可以参与聚合 Numerical：数值类型，分两类 

​	基本数据类型：long、interger、short、byte、double、float、half_float 

​	浮点数的高精度类型：scaled_float 需要指定一个精度因子，比如10或100。

​	elasticsearch会把真实值乘以这个因子后存 储，取出时再还原。 

​	Date：日期类型 elasticsearch可以对日期格式化为字符串存储，但是建议我们存储为毫秒值，存储为long， 节省空间。

##### 获取当前索引的setting信息

```
GET /indexName/_settings
```

##### 获取当前索引的mapping信息

```
GET /indexName/_mapping
```

##### 获取所有的索引mapping信息

```
GET /_all/_mapping
```

##### 添加数据

```
注意:新增数据时如果使用自定义id那使用put请求,使用随机id使用post请求
POST /索引名称/_doc/1 -----  1指定Id   不写就是不指定 会生成随机Id
{
"name":"赵俊浩",
"age":18,
"birthday":"2020-09-02"
}

```

##### 松散的列设计

事实上Elasticsearch非常智能，你不需要给索引库设置任何mapping映射，如果在增加有没有提前定义的属性字段，根据属性值自动创建

##### 新增数据

```
POST / 索引名称/_doc/id

{

	"name":"你好世界"

	"sex":"男"

}
```

##### 修改数据

```
PUT / 索引名称/_doc/id

{

	"name":"你好世界"

	"sex":"男"
	
	"修改字段名":"****"

}
```

##### 删除数据

```
DELETE /student/_doc/1 (删除的Id值)
```

##### 基本查询

​	

```
GET /索引库名/_search
{
    "query":{
        "查询类型":{
        "查询条件":"查询条件值"
        }
    }
}
```

查询类型： 例如： match_all ， match ， term ， range 等等 查询条件：查询条件会根据类型的不同，写法也有差异，后面详细讲解

##### 查询所有（match_all)

```
GET /student/_search
    {
    "query":{
    	"match_all": {}
    }
}
```

query ：代表查询对象 

match_all ：代表查询所有

查询出来的信息参数

```
took：查询花费时间，单位是毫秒
time_out：是否超时
_shards：分片信息
hits：搜索结果总览对象
total：搜索到的总条数
max_score：所有结果中文档得分的最高分
hits：搜索结果的文档对象数组，每个元素是一条搜索到的文档信息
_index：索引库
_type：文档类型
_id：文档id
_score：文档得分
_source：文档的源数据

```

#####  匹配查询（match）

​	批量新增

​		

```
POST /goods/_doc/_bulk
{"index":{}}
{"title":"华为手机","description":"这个是华为 mate 30 pro手机","price":5000.00,"stock":100}
格式如上 照葫芦画瓢
```

or关系

match 类型查询，会把查询条件进行分词，然后进行查询,多个词条之间是or的关系

```
GET /goods/_search
{
    "query": {
        "match": {
        "title": "华为手机 "
        }
    }
}
```

and关系

某些情况下，我们需要更精确查找，我们希望这个关系变成 and ，可以这样做：

```
GET /goods/_search
{
    "query": {
        "match": {
        	"title": {"query": "华为手机","operator": "and"}
        }
    }
}
```

or和and之间？

在 or 与 and 间二选一有点过于非黑即白。 如果用户给定的条件分词后有 5 个查询词项，想查找只包 含其中 4 个词的文档，该如何处理？将 operator 操作符参数设置成 and 只会将此文档排除。 有时候这正是我们期望的，但在全文搜索的大多数应用场景下，我们既想包含那些可能相关的文档，同 时又排除那些不太相关的。换句话说，我们想要处于中间某种结果。 match 查询支持 minimum_should_match 最小匹配参数， 这让我们可以指定必须匹配的词项数用来 表示一个文档是否相关。我们可以将其设置为某个具体数字，更常用的做法是将其设置为一个 百分数 ， 因为我们无法控制用户搜索时输入的单词数量：

```
GET /goods/_search
{
    "query": {
        "match": {
        "title": {"query": "华为5g手机","minimum_should_match": "50%"}
        }
    }
}
```

##### 多字段查询(multi_match)

multi_match 与 match 类似，不同的是它指定在多个字段中查询这个关键字

```
GET /goods/_search
{
    "query": {
        "multi_match": {
        "query": "手机",
        "fields": ["title","description"]
        }
    }
}
```

##### 词条匹配(term)

term 查询被用于精确值 匹配，这些精确值可能是数字、时间、布尔或者那些未分词的字符串

```
GET /goods/_search
{
    "query":{
        "term":{
        "price":2000
        }
    }
}
```

##### 多词条精确匹配(terms)

terms 查询和 term 查询一样，但它允许你指定多值进行匹配。如果这个字段包含了指定值中的任何一 个值，那么这个文档满足条件

```
GET /goods/_search
{
    "query":{
        "terms": {
            "price": [
                2000,
                5000
            ]
        }
    }
}

```

#####  结果过滤

默认情况下，elasticsearch在搜索的结果中，会把文档中保存在 _source 的所有字段都返回。 如果我们只想获取其中的部分字段，我们可以添加 _source 的过滤

直接指定返回字段

```
GET /goods/_search
{
    "_source": ["title","description"],
        "query":{
            "terms": {
            "price": [
                2000,
                5000
            ]
        }
    }
}
```

##### 指定includes和excludes

如果要查询的字段或者 不要字段过多，可以通过 包含includes， 或者 排除excludes， includes：来指定想要显示的字段 excludes：来指定不想要显示的字段 二者都是可选的。

```
GET /goods/_search
{
    "_source": {
    "excludes":["title","stock"]
        },
            "query":{
            "terms": {
            "price": [
            5000,
            2000
            ]
        }
    }
}

```

##### 高级查询

##### 布尔组合（bool)

bool 把各种其它查询通过 must （与）、 must_not （非）、 should （或）的方式进行组合

```json
//相当于&
GET /goods/_search
{
    "query": {
        "bool": {
            "must": [{
                "match": {
                    "title": "华为"
                    }
                   },
                    {
                    "match": {
                    "description": "pro"
                    }
                }
            ]
        }
    }
}

//相当于!

GET /goods/_search
{
    "query": {
        "bool": {
            "must_not": [
                {
                    "match": {
                    	"title": "oppo"
                    }
                }
            ]
        }
    }
}
//相当于||

GET /goods/_search
{
    "query": {
        "bool": {
            "should": [
            {
                "match": {
                    "description": "华为"
                    }
                   },
                    {
                    "match": {
                    "description": "apple"
                    }
                }
            ]
        }
    }
}
//组合使用

GET /goods/_search
{
	"query": {
		"bool":
		"must": [
			{
            "match": {
            "title": "华为"
            }
		},
	{
    "match": {
    "description": "pro"
    	}
   	  }
	],
"must_not": [
    {
        "match": {
            "title": "oppo"
        }
	}
],
"should": [
{
"match": {
    "description": "华为"
    }
},
{
"match": {
"description": "apple"
				}
			}
			]
		}
	}
}

```

##### 范围查询(range)

range 区间查询，值 >= 条件 <= 值

```json
GET /goods/_search
{
    "query": {
        "range": {
            "price": {
            "gte": 2000,
            "lte": 4999
            }
        }
    }
}

```

range 查询允许以下字符：

![image-20210311220150029](C:\Users\Angelina\AppData\Roaming\Typora\typora-user-images\image-20210311220150029.png)

##### 模糊查询(fuzzy)

fuzzy 查询是 term 查询的模糊等价。它允许用户搜索词条与实际词条的拼写出现偏差，但是偏差的 编辑距离不得超过2：

```
GET /goods/_search
{
    "query": {
        "fuzzy": {
        	"title":"华为路"
        }
    }
}

```

我们可以通过 fuzziness 来指定允许的编辑容错距离：

```
GET /goods/_search
{
    "query": {
        "fuzzy": {
            "title":{
                "value":"华为路由",
                "fuzziness": 2
            }
        }
    }
}	
```

##### 过滤(filter)

条件查询中进行过滤 所有的查询都会影响到文档的评分及排名。如果我们需要在查询结果中进行过滤，并且不希望过滤条件 影响评分，那么就不要把过滤条件作为查询条件来用。而是使用 filter 方式： 查询条件会影响文档数据的成绩优先级，如果有的条件 不希望影响成绩优先级，那么用fliter方法

```
GET /goods/_search
{
    "query": {
        "bool": {
            "must":
            {
                "match": {
               		"title": "华为手机"
                	}
                }
                ,
                "filter": {
                    "range": {
                        "price": {
                            "gte": 2000,
                            "lte": 5000
               		}
                }
            }
        }
    }
}	
```

注意： filter 中还可以再次进行 bool 组合条件过滤。 两次结果筛选到的数据是一样的，但是成绩是不一样的，不使用filter会加重成绩得分

无查询条件，直接过滤 如果一次查询只有过滤，没有查询条件，不希望进行评分，我们可以使用 constant_score 取代只有 filter 语句的 bool 查询。在性能上是完全相同的，但对于提高查询简洁性和清晰度有很大帮助。

```
GET /goods/_search
{
"query": {
    "constant_score": {
       "filter": {
        	"range": {
                "price": {
                "gte": 2000,
                "lte": 5000
        		}
        	}
        },
        "boost": 1
        }
    }
}
//boost 为权重 决定了当前查询结果能的多少分

```

##### 排序

##### 单字段排序

指定排序方式 sort 可以让我们按照不同的字段进行排序，并且通过 order 指定排序的方式

```
GET /goods/_search
{
    "query": {
        "match": {
        	"title": "华为手机"
        }
    },
    "sort": [
        {
            "price": {
            	"order": "desc"
            }
        }
    ]
}

```

##### 多字段排序

多个组合成一个字段进行排序 使用 title分词查询，匹配的结果首先按照评分排序，然后按照价格排序：

```
GET /goods/_search
{
"query": {
    "match": {
    	"title": "华为手机"
    	}
    },
    "sort": [
        {
        "_score": {
        	"order": "desc"
        }
    },
        {
        	"price": {"order": "desc"}
        }
    ]
}
```

##### 聚合aggregations

聚合可以让我们极其方便的实现对数据的统计、分析。例如： 每个月手机的销量？ 5000价格以上手机的平均价格？ 每种品牌下的手机有几种？ 实现这些统计功能的比数据库的sql要方便的多，而且查询速度非常快，可以实现近实时搜索效果。

##### 基本概念

Elasticsearch中的聚合，包含多种类型，最常用的两种，一个叫 桶 ，一个叫 度量 ： 

桶（bucket） 桶的作用，是按照某种方式对数据进行分组，每一组数据在ES中称为一个 桶 ，例如我们根据国籍对人 划分，可以得到 中国桶 、 英国桶 ， 日本桶 ……或者我们按照年龄段对人进行划分： 0~10,10~20,20~30,30~40等。

 Elasticsearch中提供的划分桶的方式有很多：

 Date Histogram Aggregation：根据日期阶梯分组，例如给定阶梯为周，会自动每周分为一组 Histogram Aggregation：根据数值阶梯分组，与日期类似 Terms Aggregation：

根据词条内容分组，词条内容完全匹配的为一组 Range Aggregation：数值和日期的范围分组，指定开始和结束，然后按段分组 …… 综上所述，我们发现bucket aggregations 只负责对数据进行分组，并不进行计算，因此往往bucket中 往往会嵌套另一种聚合：metrics aggregations即度量 度量（metrics） 分组完成以后，我们一般会对组中的数据进行聚合运算，例如求平均值、最大、最小、求和等，

这些在 ES中称为 度量 比较常用的一些度量聚合方式： 

Avg Aggregation：求平均值 

Max Aggregation：求最大值 

Min Aggregation：求最小值 

Percentiles Aggregation：求百分比 

Stats Aggregation：同时返回avg、max、min、sum、count等 Sum Aggregation：求和 

Top hits Aggregation：求前几 Value Count Aggregation：求总数 ……

##### 聚合为桶

首先，我们按照 汽车的颜色 color来 划分 桶 等同与sql中group的概念

```
GET /cars/_search
{
    "size": 0,
        "aggs": {
            "gro_color": {
            "terms": {
            	"field": "color",
            	"size": 10
            }
        }
    }
}
```

size：查询的数据条数，0代表一条也不展示

aggs：声明这是一个聚合查询，是aggregations的缩写 

​		gro_color：给这次聚合起一个名字，任意。 

​				terms：划分桶的方式，这里是根据词条划分 

​						field：划分桶的字段 Size:默认取10条数据，现在只有4个颜色，所以说全部取出，也可以删掉

hits：查询结果 

aggregations：聚合的结果

 gro_color：我们定义的聚合分组名称

 buckets：查找到的桶，每个不同的color字段值都会形成一个桶 key：这个桶对应的color字段的值 doc_count：这个桶中的文档数量 通过聚合的结果我们发现，目前红色数据最多

##### 桶内度量

前面的例子告诉我们每个桶里面的文档数量，这很有用。 但通常，我们的应用需要提供更复杂的文档度 量。 例如，每种颜色下车辆的最高价格， 因此，我们需要告诉Elasticsearch 使用哪个字段 ， 使用何种度量方式 进行运算，这些信息要嵌套在 桶 内， 度量 的运算会基于 桶 内的文档进行 现在，我们为刚刚的聚合结果添加 求价格最高值的度量： 等同于 sql中 聚合函数的概念

```
GET /cars/_search
{
    "size": 0,
        "aggs": {
            "gro_color": {
                "terms": {
                	"field": "color"
                },
                "aggs": {
                "max_price": {
                    "max": {
                    "field": "price"
                	}
            	}
            }
        }
    }
}
```

aggs：我们在上一个aggs(popular_colors)中添加新的aggs。可见度量也是一个聚合

max_price：聚合的名称

max：度量的类型，这里是求最大值（还有min avg sum 等）

field：度量运算的字段

##### 桶内嵌套桶

刚刚的案例中，我们在桶内嵌套度量运算。事实上桶不仅可以嵌套运算， 还可以再嵌套其它桶。也就是 说在每个分组中，再分更多组。 比如：我们想统计每种颜色的汽车中，分别属于哪个制造商，按照 make 字段再进行分桶

```
GET /cars/_search
{
    "size": 0,
        "aggs": {
            "gro_color": {
                "terms": {
                	"field": "color"
                },
                "aggs": {
                "max_price": {
                	"max": {
                		"field": "price"
               		}
                },
                "brand":{
                    "terms":{
                    	"field":"make"
                    }
                }
            }
        }
    }
}
```

原来的color桶和max计算我们不变 

make：在嵌套的aggs下新添一个桶，叫做brand 

terms：桶的划分类型依然是词条 

filed：这里根据make字段进行划分

我们可以看到，新的聚合 make 被嵌套在原来每一个 color 的桶中。 每个颜色下面都根据 make 字段进行了分组 我们能读取到的信息： 红色车共有4辆 红色车最贵的车价格是1300000 其中3辆是 大众制造，1辆是宝马制造。

##### 划分桶的其它方式

前面讲了，划分桶的方式有很多，例如：

 Date Histogram Aggregation：根据日期阶梯分组，例如给定阶梯为周，会自动每周分为一组

 Histogram Aggregation：根据数值阶梯分组，与日期类似 

Terms Aggregation：根据词条内容分组，词条内容完全匹配的为一组 

Range Aggregation：数值和日期的范围分组，指定开始和结束，

然后按段分组 上方的案例中，我们采用的是Terms Aggregation，词条划分桶。

接下来，我们再学习几个比较实用的：

##### 阶梯分桶Histogram

 原理： histogram是把数值类型的字段，按照一定的阶梯大小进行分组。你需要指定一个阶梯值（interval）来 划分阶梯大小。

把汽车价格区间作为分组条件，看下哪个价格区间的汽车数量，或者最高价等

操作一下： 比如，我们对汽车的价格进行分组，指定间隔interval为100000： min_doc_count:去除没有记录的结果

##### 范围分桶range

范围分桶与阶梯分桶类似，也是把数字按照阶段进行分组，只不过range方式需要你自己指定每一组的 起始和结束大小。

#### Spring Data Elasticsearch

##### 

Elasticsearch提供的Java客户端有一些不太方便的地方：

 很多地方需要拼接Json字符串，在java中拼接字符串有多恐怖你应该懂的 

需要自己把对象序列化为json存储 

查询到结果也需要自己反序列化为对象 

因此，我们这里就不讲解原生的Elasticsearch客户端API了。

 而是学习Spring提供的套件：Spring Data Elasticsearch

##### 简介

Spring Data Elasticsearch是Spring Data项目下的一个子模块。 

查看 Spring Data的官网：https://spring.io/projects/spring-data

Spring Data

Spring Data的任务是为数据访问提供一个熟悉且一致的，基于Spring的编程模型，同时仍保留基础数 据存储的特殊特征。它使使用数据访问技术，关系和非关系数据库，map-reduce框架以及基于云的数据服务变得容易。这 是一个总括项目，其中包含许多特定于给定数据库的子项目。这些项目是与这些令人兴奋的技术背后的 许多公司和开发人员共同开发的。

Spring Data Elasticsearch

用于Elasticsearch的Spring Data是Spring Data项目的一部分，该项目旨在为新数据存储提供熟悉且一 致的基于Spring的编程模型，同时保留特定于存储的功能。

Spring Data Elasticsearch项目提供了与Elasticsearch搜索引擎的集成。Spring Data Elasticsearch的 关键功能区域是以POJO为中心的模型，用于与Elastichsearch文档进行交互并轻松编写存储库样式数据 访问层。

 特征 

Spring配置支持使用基于Java的 @Configuration 类或ES客户端实例的XML名称空间。 

ElasticsearchTemplate 帮助程序类，可提高执行常规ES操作的效率。包括文档和POJO之间的 集成对象映射。 

与Spring的转换服务集成的功能丰富的对象映射 

基于注释的映射元数据，但可扩展以支持其他元数据格式

 Repository 接口的自动实现，包括对自定义查找器方法的支持。 

CDI对存储库的支持

#### 项目实用

##### 	新建项目 esdemo

pom.xml

```
<parent>

    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.1.RELEASE</version>
</parent>

<dependencies>

    <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    </dependency>
    
    <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    </dependency>
    
</dependencies>

```

##### application.yml

```
spring:
	elasticsearch:
		rest:
			uris: 119.45.191.248:9200
```

##### 启动类

```
@SpringBootApplication
public class RunTestEsApplication {
    public static void main(String[] args) {
    	SpringApplication.run(RunTestEsApplication.class);
    }
}

```

##### entity

```java
@Document(indexName = "goods",shards = 1,replicas = 0)
@Data
public class GoodsDoc {
    @Id
    private Long id;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String brandName;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String categoryName;
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;
    private Long brandId;
    private Long cid1;
    private Long cid2;
    private Long cid3;
    private Date createTime;
    private List<Long> price;
    @Field(type = FieldType.Keyword, index = false)
    private String skus;
    //规格
    private Map<String, Object> specs;
}
```

##### 新建测试类

测试类上的注解

```
//让测试在Spring容器环境下执行
@RunWith(SpringRunner.class)
//声明启动类,当测试方法运行的时候会帮我们自动启动容器
@SpringBootTest(classes = { RunTestEsApplication.class})
```

##### 测试

创建索引

```
@Autowired
private ElasticsearchRestTemplate elasticsearchRestTemplate;
/*
创建索引
*/
@Test
public void createGoodsIndex(){
IndexOperations 						indexOperations=elasticsearchRestTemplate.indexOps(IndexCoordinates.of("indexname"));
	indexOperations.create();//创建索引
	//indexOperations.exists() 判断索引是否存在
System.out.println(indexOperations.exists()?"索引创建成功":"索引创建失败");

}

```

##### 创建映射

```
/*
创建映射
*/
@Test
public void createGoodsMapping(){
    //此构造函数会检查有没有索引存在,如果没有则创建该索引,如果有则使用原来的索引
    IndexOperations indexOperations =
    elasticsearchRestTemplate.indexOps(GoodsEntity.class);
    //indexOperations.createMapping();//创建映射,不调用此函数也可以创建映射,这就是
    高版本的强大之处
    System.out.println("映射创建成功");
}
```

##### 删除索引

```
/*
删除索引
*/
@Test
public void deleteGoodsIndex(){
IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(GoodsEntity.class);
indexOperations.delete();
System.out.println("索引删除成功");
}

```

##### 新增文档

##### GoodsEsRepository

```
import com.mr.entity.GoodsEntity;
import
org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
/**
* @ClassName GoodsEsRepository
* @Description: TODO
* @Author shenyaqi
* @Date 2020/9/3
* @Version V1.0
**/
public interface GoodsEsRepository extendsElasticsearchRepository<GoodsEntity,Long> {
}

```

##### 注入repository

```
@Resource
private GoodsEsRepository goodsEsRepository;

```

##### 新增

```
/*
新增文档
*/
@Test
public void saveData(){
    GoodsEntity entity = new GoodsEntity();
    entity.setId(1L);
    entity.setBrand("小米");
    entity.setCategory("手机");
    entity.setImages("xiaomi.jpg");
    entity.setPrice(1000D);
    entity.setTitle("小米3");
    goodsEsRepository.save(entity);
    System.out.println("新增成功");
}
```

##### 批量新增

```
/*
批量新增文档
*/
@Test
public void saveAllData(){
    GoodsEntity entity = new GoodsEntity();
    entity.setId(2L);
    entity.setBrand("苹果");
    entity.setCategory("手机");
    entity.setImages("pingguo.jpg");
    entity.setPrice(5000D);
    entity.setTitle("iphone11手机");
    GoodsEntity entity2 = new GoodsEntity();
    entity2.setId(3L);
    entity2.setBrand("三星");
    entity2.setCategory("手机");
    entity2.setImages("sanxing.jpg");
    entity2.setPrice(3000D);
    entity2.setTitle("w2019手机");
    GoodsEntity entity3 = new GoodsEntity();
    entity3.setId(4L);
    entity3.setBrand("华为");
    entity3.setCategory("手机");
    entity3.setImages("huawei.jpg");
    entity3.setPrice(4000D);
    entity3.setTitle("华为mate30手机");
    goodsEsRepository.saveAll(Arrays.asList(entity,entity2,entity3));
    System.out.println("批量新增成功");
}
```

#####  更新文档

```
/*
更新文档
*/
@Test
public void updateData(){
    GoodsEntity entity = new GoodsEntity();
    entity.setId(1L);
    entity.setBrand("小米");
    entity.setCategory("手机");
    entity.setImages("xiaomi.jpg");
    entity.setPrice(1000D);
    entity.setTitle("小米3");
    goodsEsRepository.save(entity);6.2.7.7 删除文档

    System.out.println("修改成功");
}

```

##### 删除文档

```
/*
删除文档
*/
@Test
public void delData(){
    GoodsEntity entity = new GoodsEntity();
    entity.setId(1L);
    goodsEsRepository.delete(entity);
    System.out.println("删除成功");
}

```

##### 查询所有

```
/*
查询所有
*/
@Test
public void searchAll(){
    //查询总条数
    long count = goodsEsRepository.count();
    System.out.println(count);
    //查询所有数据
    Iterable<GoodsEntity> all = goodsEsRepository.findAll();
    all.forEach(goods -> {
    System.out.println(goods);
    });
}

```

##### 条件查询

##### GoodsEsRepository

```
List<GoodsEntity> findAllByAndTitle(String title);
List<GoodsEntity> findByAndPriceBetween(Double start,Double end);

```

##### 条件查询

```
/*
条件查询
*/
@Test
public void searchByParam(){
    List<GoodsEntity> allByAndTitle =
    goodsEsRepository.findAllByAndTitle("手机");
    System.out.println(allByAndTitle);6.2.7.10 自定义查询
    System.out.println("===============================");
    List<GoodsEntity> byAndPriceBetween =
    goodsEsRepository.findByAndPriceBetween(1000D, 3000D);
    System.out.println(byAndPriceBetween);
}
```

##### 自定义查询

```
/*
自定义查询
*/
@Test
public void customizeSearch(){
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    queryBuilder.withQuery(
        QueryBuilders.boolQuery()
        .must(QueryBuilders.matchQuery("title","华为"))
        .must(QueryBuilders.rangeQuery("price").gte(1000).lte(10000))
    );
    //排序
    queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
    //分页
    //当前页 -1
        queryBuilder.withPageable(PageRequest.of(0,10));
        SearchHits<GoodsEntity> search =
        elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);
        search.getSearchHits().stream().forEach(hit -> {
        System.out.println(hit.getContent());
    });
}

```

高亮

```
/*
高亮
*/
@Test
public void customizeSearchHighLight(){
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    //构建高亮查询
    HighlightBuilder highlightBuilder = new HighlightBuilder();
    
    HighlightBuilder.Field title = new HighlightBuilder.Field("title");
    
    title.preTags("<span style=color:red'>");
    
    title.postTags("</span>");
    
    highlightBuilder.field(title);
    
    queryBuilder.withHighlightBuilder(highlightBuilder);//设置高亮
    
    queryBuilder.withQuery(
        QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery("title","华为手机"))
            .must(QueryBuilders.rangeQuery("price").gte(1000).lte(10000))
    );
    queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
    
    queryBuilder.withPageable(PageRequest.of(0,2));
    
    SearchHits<GoodsEntity> search = elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);
    
    List<SearchHit<GoodsEntity>> searchHits = search.getSearchHits();
    
    //重新设置title
    List<SearchHit<GoodsEntity>> result = searchHits.stream().map(hit -> {
    
        Map<String, List<String>> highlightFields =
        hit.getHighlightFields();
        hit.getContent().setTitle(highlightFields.get("title").get(0));
        return hit;
    
    }).collect(Collectors.toList());
    System.out.println(result);
}
```



##### 高亮封装

```
package com.baidu.shop.utils;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName HighLightUtil
 * @Description: TODO
 * @Author wanglonglong
 * @Date 2021/3/3
 * @Version V1.0
 **/
public class HighLightUtil {

    public static HighlightBuilder getHighlightBuilder(String ...field){
        HighlightBuilder highlightBuilder = new HighlightBuilder();

        Arrays.asList(field).stream().forEach(s -> {
            highlightBuilder.field(s);
            highlightBuilder.preTags("<font style='color:red'>");
            highlightBuilder.postTags("</font>");

        });

        return highlightBuilder;
    }


    public static <T> List<T> getHighlightList(List<SearchHit<T>> searchHits){

        //修改title
        return searchHits.stream().map(entitySearchHit -> {
            T content = entitySearchHit.getContent();

            Map<String, List<String>> highlightFields = entitySearchHit.getHighlightFields();

            highlightFields.forEach((key,value)->{

                try {
                    Method method = content.getClass().getMethod("set" + firstCharUpper(key), String.class);

                    method.invoke(content,value.get(0));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });

            return content;
        }).collect(Collectors.toList());

    }


    public static String firstCharUpper(String str){
        char[] chars = str.toCharArray();

        chars[0] -= 32;

        return String.valueOf(chars);
    }


}

```

#####  聚合

##### 	聚合为桶

桶就是分组，比如这里我们按照品牌brand进行分组：

```
@Test
public void searchAgg(){
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
    queryBuilder.addAggregation(
    AggregationBuilders.terms("brand_agg").field("brand")
);

SearchHits<GoodsEntity> search =elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);

    Aggregations aggregations = search.getAggregations();
        //terms 是Aggregation的子类
        //Aggregation brand_agg = aggregations.get("brand_agg");/
        Terms terms = aggregations.get("brand_agg");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        buckets.forEach(bucket -> {
        System.out.println(bucket.getKeyAsString() + ":" +
        bucket.getDocCount());
    });
    System.out.println(search);
}
```

##### 嵌套聚合，聚合函数值

```
/*
聚合函数
*/
@Test
public void searchAggMethod(){
	NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
queryBuilder.addAggregation(
	AggregationBuilders.terms("brand_agg")
		.field("brand")
		//聚合函数
		.subAggregation(AggregationBuilders.max("max_price").field("price"))
);
SearchHits<GoodsEntity> search =elasticsearchRestTemplate.search(queryBuilder.build(), GoodsEntity.class);

Aggregations aggregations = search.getAggregations();

Terms terms = aggregations.get("brand_agg");

List<? extends Terms.Bucket> buckets = terms.getBuckets();

buckets.forEach(bucket -> {

System.out.println(bucket.getKeyAsString() + ":" + bucket.getDocCount());

//获取聚合
Aggregations aggregations1 = bucket.getAggregations();

//得到map
Map<String, Aggregation> map = aggregations1.asMap();

//需要强转,Aggregations是一个类 Terms是他的子类,Aggregation是一个接口Max是
他的子接口,而且Max是好几个接口的子接口

        Max max_price = (Max) map.get("max_price");
        System.out.println(max_price.getValue());
	});
}
```

