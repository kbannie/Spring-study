package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonWithPrototypeTest1 {
    @Test
    void prototypeFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);

    }

    @Test
    void singletonClientUsePrototype(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ClinetBean.class);
        ClinetBean clientBean1 = ac.getBean(ClinetBean.class);
        int count1=clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClinetBean clientBean2 = ac.getBean(ClinetBean.class);
        int count2=clientBean2.logic();
        assertThat(count2).isEqualTo(2);

    }

    @Scope("singleton")
    static class ClinetBean{
        private final PrototypeBean prototypeBean; //생성 시점에 주입

        @Autowired
        public ClinetBean(PrototypeBean prototypeBean){
            this.prototypeBean=prototypeBean;
        }

        public int logic(){
            prototypeBean.addCount();
            int count= prototypeBean.getCount();
            return count;
        }


    }

    @Scope("prototype")
    static class PrototypeBean{
        private int count=0;
        public void addCount(){
            count++;
        }

        public int getCount(){
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("prototypeBEan.init"+this);
        }

        @PreDestroy
        public void destory(){
            System.out.println("PrototypeBEan.destory");
        }
    }
}
