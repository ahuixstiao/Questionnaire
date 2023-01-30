package com.questionnaire.api;

import com.questionnaire.db.mapper.system.TableInfoMapper;
import com.questionnaire.db.service.IAnswerQuestionnaireService;
import com.questionnaire.db.service.IQuestionnaireService;
import com.questionnaire.gen.CodeGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest(classes = TeaCupApplication.class)
// 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
class TeaCupApplicationTests {

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private TableInfoMapper tableInfoMapper;

    @Autowired
    private IQuestionnaireService questionnaireService;

    @Autowired
    private IAnswerQuestionnaireService answerQuestionnaireService;


    @Test
    public void testing() throws Exception {
        /*String str = "op1,op2,op3";
        List<String> strings = Arrays.asList(str.split(","));*/

        //List<Users> users = usersService.lambdaQuery().list();
        //esUsersMapper.insertBatch(users);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        /*Result<Object> objectResult = answerQuestionnaireService.queryAnswerQuestionnaireList(1, 10);
        System.out.println(objectResult);*/
        /*Result<Object> objectResult = questionnaireService.queryQuestionnaireListByFlag(2, 2, 1);
        System.out.println(objectResult);*/

        stopWatch.stop();
        System.out.println("Execution time: " + stopWatch.getTotalTimeMillis() + " milliseconds");
    }

    //代码生成测试
    @Test
    public void generateCodeTest() {
        //codeGenerator.codeGenerator("q_answer_questionnaire");
    }

}