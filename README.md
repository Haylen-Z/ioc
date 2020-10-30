# ioc
spring ioc 小轮子

用注解装饰依赖的类，支持单例（默认）和原型两个作用域：

    @Bean
    class A {
      @Bean(name = "b", scope = Scope.PROTOTYPE)
      public static B getB() {
        return new B();
      }
    }
  
@Bean注解用在方法方法上时，只能是静态方法。

使用@Inject注解进行依赖注入，或是构造器注入：

    @Bean
    class C {
      @Inject
      private A a；
      private B b;

      public C(B b) {
        this.b = b;
      }
    }
