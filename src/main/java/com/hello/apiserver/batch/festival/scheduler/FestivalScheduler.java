package com.hello.apiserver.batch.festival.scheduler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.hello.apiserver.batch.festival.mapper.FestivalMapper;
import com.hello.apiserver.batch.festival.vo.FestivalVo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Configuration            //기본 설정 선언
@EnableBatchProcessing     //기본 설정 선언
@EnableScheduling          //스케줄러 사용 선언
public class FestivalScheduler { //http://www.javainuse.com/spring/bootbatch 참고

    private static final String BATCH_NAME = "BaseJobStep";

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
//    @Autowired
//    private SimpleJobLauncher jobLauncher;

//    @Autowired
//    FestivalRepository festivalRepository;

    @Autowired
    FestivalMapper festivalMapper;

    /**
     * 스케쥴러
     *
     * 주기적으로 Job 실행
     * cron 설정에 따라 실행
     *  jobLauncher 1개당 job 1+ 와 step 1+ 를 사용가능
     *  매일 자정에 배치 실행.(UTC+9 기준. UTC 기준 9시)
     * @throws Exception
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void scheduler() throws Exception {
//        String jobId = String.valueOf(System.currentTimeMillis());
//
//        System.out.println("Started jobId : "+ jobId);
//
//        JobParameters param = new JobParametersBuilder()
//                .addString("JobID", jobId).toJobParameters();
//        JobExecution execution = jobLauncher.run(baseJob(), param);
//
//        System.out.println("end : " + param.getString("JobID") +":::"+ execution.getStatus());

        final String SERVICE_KEY = "zM161Sab%2F0yQtw50j%2FwiyqmK%2B8tLV6DpMJ1if8XfM4sGB%2BmalYfBn4Hbv6YBawFhWM1f0Rnxl5s7oBpARmIcfg%3D%3D";

        final int NUM_OF_ROWS = 999;
        final int AREA_CODE = 39;
        final String MOBILE_APP = "jejumate";

        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = calendar.getTimeZone();
        timeZone.setID("Asia/Seoul");
        calendar.setTimeZone(timeZone);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        String eventStartDate = String.format("%d%02d%02d", year, month, dayOfMonth);

        try {
            String url = String.format("http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchFestival?" +
                    "serviceKey=%s" +
                    "&numOfRows=%d" +
                    "&pageSize=%d" +
                    "&pageNo=1" +
                    "&startPage=1" +
                    "&MobileOS=ETC" +
                    "&MobileApp=%s" +
                    "&arrange=A" +
                    "&listYN=Y" +
                    "&areaCode=%d" +
                    "&eventStartDate=%s" +
                    "&_type=json", SERVICE_KEY, NUM_OF_ROWS, NUM_OF_ROWS, MOBILE_APP, AREA_CODE, eventStartDate);

            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
//            System.out.println(response.toString()); //결과, json결과를 parser하여 처리

//            Gson gson = new Gson();
//            gson.toJsonTree()

            JsonElement jsonElement = new JsonParser().parse(response.toString());
            String festivalItems = jsonElement.getAsJsonObject().getAsJsonObject("response").getAsJsonObject("body").getAsJsonObject("items").getAsJsonArray("item").toString();

//            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                @Override
//                public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
//                    return new Date(json.getAsJsonPrimitive().getAsLong());
//                }
//            }).create();
            Gson gson = new GsonBuilder().setDateFormat("yyyyMMddHHmmss").create();

            List<FestivalVo> festivalList = gson.fromJson(festivalItems, new TypeToken<List<FestivalVo>>() {}.getType());
//            this.festivalRepository.save(festivalList);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("festivalList", festivalList);

            this.festivalMapper.saveFestivalInfoList(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 배치 Job
     *
     * baseStep 호출한다
     *
     * @return
     */
    @Bean
    public Job baseJob() {
        return jobBuilderFactory.get("[Job - " + BATCH_NAME + "]")
                .start(baseStep()).build();
    }

    /**
     * 배치 Step
     *
     * <pre>
     * reader() : 더미 데이터 생성
     * writer() : sysout 찍기
     * </pre>
     *
     * @return
     */
    @Bean
    public Step baseStep() { //chunk 큰덩어리 프로세스단위
        return stepBuilderFactory.get("[Step - " + BATCH_NAME + "]")
                .<String, String>chunk(20).reader(sampleItemReader())
                .processor(sampleItemProcessor()).writer(sampleItemWriter())
                .build();
    }

    @Bean
    public ItemReader<String> sampleItemReader() {

        ItemReader<String> reader = new ItemReader<String>() {
            String[] messages = { "sample data",
                    "Welcome to Spring Batch Example",
                    "Database for this example" };
            int count = 0;
            @Override
            public String read() throws Exception, UnexpectedInputException
                    , ParseException, NonTransientResourceException {
                if (count < messages.length) {
                    return messages[count++];
                } else {
                    count = 0;
                }
                return null;
            }
        };


        return reader;
    }

    @Bean
    public ItemProcessor<String, String> sampleItemProcessor() {
        return new ItemProcessor<String, String>() {

            @Override
            public String process(String data) throws Exception {
                return data.toUpperCase();
            }
        };
    }

    @Bean
    public ItemWriter<String> sampleItemWriter() {
        ItemWriter<String> writer = new ItemWriter<String>() {

            @Override
            public void write(List<? extends String> items) throws Exception {
                for (String msg : items) {
                    System.out.println("the data " + msg);
                }

            }
        };

        return writer;
    }
}