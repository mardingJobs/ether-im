/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ether.im.push;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;

@Component
public class ImPushServerRunner implements CommandLineRunner {

    @Autowired
    private List<ImPushServer> ImServers;

    /**
     * 判断服务是否准备完毕
     */
    public boolean isReady(){
       for (ImPushServer IMServer : ImServers){
           if (!IMServer.ready()){
               return false;
           }
       }
       return true;
    }

    @Override
    public void run(String... args) throws Exception {
        //启动每个服务
        if (!CollectionUtil.isEmpty(ImServers)){
            ImServers.forEach(ImPushServer::start);
        }
    }

    @PreDestroy
    public void destroy(){
        if (!CollectionUtil.isEmpty(ImServers)){
            ImServers.forEach(ImPushServer::shutdown);
        }
    }
}
