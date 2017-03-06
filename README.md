#Prototype架构设计

领域驱动设计指导我们如何分析并模型化复杂的业务；敏捷方法论帮助我们消除浪费，快速反馈；持续交付促使我们构建更快、更可靠、更频繁的软件部署和交付能力；虚拟化和基础设施自动化(Infrastructure As Code)则帮助我们简化环境的创建、安装；DevOps 文化的流行以及特性团队的出现，使得小团队更加全功能化。这些都是推动微服务诞生的重要因素。

Prototype在领域模型的划分和设计上采用领域驱动设计([DDD](https://en.wikipedia.org/wiki/Domain-driven_design))，在业务服务化方面采用微服务([Microservices](https://en.wikipedia.org/wiki/Microservices))架构风格。


---
##Prototype逻辑分层

###Prototype逻辑分层概述

- prototype-business-domain

       负责表示业务概念、业务状况的信息以及业务规则。尽管保存这些内容的技术细节由基础结构层来完成，反映业务状况的状态在该层中被控制和使用。这一层是业务软件的核心。该层对应DDD中的领域模型层。

- prototype-business-application

       以业务用例为单位向外暴露该系统的业务功能，对应DDD中的应用层（Application Layer）。应用层是领域模型的直接客户。然而，应用层中不应该包含有业务逻辑，否则就造成了领域逻辑的泄漏，而应该是很薄的一层，主要起到协调的作用，它所做的只是将业务操作代理给下层的领域模型。同时，如果我们的业务操作有事务需求，那么对于事务的管理应该放在应用层上，因为事务也是以业务用例为单位的。

- prototype-business-edge

       统一对外提供基本业务接口，或者提供经过组合和流程编排后的粗粒度业务接口。Business Edge层也负责与其他系统的应用层进行交互。外界与领域模型的交互都通过Business Edge层完成。

- prototype-presentation-web

       负责向用户显示信息，并且解析用户命令。外部的执行者有时可能会是其他的计算机系统，不一定非是人。该层对应DDD中的用户界面层（表示层）。
       
- prototype-common

       不属于任何一层，为business-application，business-edge和presentation-web提供统一的数据模型（类似DTO）和接口。为presentation-web提供RPC客户端存根(Stub)。

- [ibole-infrastructure](https://github.com/benson-git/ibole-infrastructure)和[ibole-microservice](https://github.com/benson-git/ibole-microservice)
     
       为Prototype提供基础支撑平台和微服务化框架。对应DDD中的基础结构层。

###Prototype逻辑分层图
![Prototype逻辑分层图](https://github.com/benson-git/wiki-docs/blob/master/images/prototype%20logic%20layer.asta.png)

---
###prototype-business-domain领域模型

- 聚合（Aggregate）


- 领域事件（Domain Event）

       领域事件由聚合发布

- 资源库（Repository）

       聚合实例通过资源库进行持久化，另外，对聚合的查找和获取也是通过资源库完成。

- 领域服务（Domain Service）
   
       在领域模型中，有些业务操作并不能自然地放到 实体或值对象上，此时我们可以使用无状态的领域服务。领域服务执行特定于领域的操作，其中可能涉及到多个领域对象。涉及到跨聚合的操作，通常也是使用领域服务来执行。


http://www.infoq.com/cn/articles/implementation-road-of-domain-driven-design/




