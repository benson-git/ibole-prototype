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

- 实体vs值对象（Entity vs Value Object）

       实体表示那些具有生命周期并且会在其生命周期中发生改变的东西；而值对象则表示起描述性作用的并且可以相互替换的概念。值对象是没有唯一标识的，他的equals()方法（比如在Java语言中）可以用它所包含的描述性属性字段来实现。但是，对于实体而言，equals()方法便只能通过唯一标识来实现了，因为即便两个实体所拥有的状态是一样的，他们依然是不同的实体。

- 聚合（Aggregate）

       聚合中所包含的对象之间具有密不可分的联系，他们是内聚在一起的。一个聚合中可以包含多个实体和值对象，因此聚合也被称为根实体。聚合是持久化的基本单位，它和资源库具有一一对应的关系。使用聚合的首要原则为在一次事务中，最多只能更改一个聚合的状态。如果一次业务操作涉及到了对多个聚合状态的更改，那么应该采用发布领域事件的方式通知相应的聚合。此时的数据一致性便从事务一致性变成了最终一致性（Eventual Consistency）。

- 领域事件（Domain Event）

       领域事件由聚合发布，它的最终接收者可以是本限界上下文中的组件，也可以是另一个限界上下文。领域事件在微服务（Micro Service）的架构中，使用最终一致性取代了事务一致性，通过领域事件的方式达到各个组件之间的数据一致性。领域事件的命名遵循英语中的“名词+动词过去分词”格式，即表示的是先前发生过的一件事情。
       领域事件的额外好处在于它可以记录发生在软件系统中所有的重要修改，这样可以很好地支持程序调试和商业智能化。另外，在CQRS架构的软件系统中，领域事件还用于写模型和读模型之间的数据同步。再进一步发展，事件驱动架构可以演变成事件源（Event Sourcing），即对聚合的获取并不是通过加载数据库中的瞬时状态，而是通过重放发生在聚合生命周期中的所有领域事件完成。

- 资源库（Repository）

       聚合实例通过资源库进行持久化，另外，对聚合的查找和获取也是通过资源库完成。资源库用于保存和获取聚合对象，在这一点上，资源库与DAO多少有些相似之处。但是，资源库和DAO是存在显著区别的。DAO只是对数据库的一层很薄的封装，而资源库则更加具有领域特征。另外，所有的实体都可以有相应的DAO，但并不是所有的实体都有资源库，只有聚合才有相应的资源库。
       将读取和写入通过不同的模型进行分离将会取得明显的收益。命令查询职责分离（Command Query Responsibility Segregation，CQRS）就是一种实现这种分离的技术：
        --命令作用于写入模型上，并且要以一致的方式进行修改，最好是在一个聚集（aggregate）上，它会产生一个或多个事件，所创建出来的事件并不是副产品，它们是命令的实际结果。
        --查询使用一个或多个读取模型，针对查询会进行一些优化。写入模型所生成的事件会被读取模型接受到，读取模型会进行更新，从而反映写入模型的状态。

- 领域服务（Domain Service）
   
       在领域模型中，有些业务操作并不能自然地放到 实体或值对象上，此时我们可以使用无状态的领域服务。领域服务执行特定于领域的操作，其中可能涉及到多个领域对象。涉及到跨聚合的操作，通常也是使用领域服务来执行。


http://www.infoq.com/cn/articles/implementation-road-of-domain-driven-design/




