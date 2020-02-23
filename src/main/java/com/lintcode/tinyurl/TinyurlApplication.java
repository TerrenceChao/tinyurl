package com.lintcode.tinyurl;

import com.lintcode.tinyurl.domain.TransformService;
import com.lintcode.tinyurl.domain._repository.MockDBRepository;
import com.lintcode.tinyurl.util.Converter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TinyurlApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		TransformService service = new TransformService(new MockDBRepository());

		// longUrl 清單
		String[] longUrlList = new String[]{
				"https://www.google.com/search?sxsrf=ALeKk00bD7Yhko8xidal3gaV51z0xZYhew%3A1582468229305&ei=hYxSXpqgEsqEr7wP246aiAg&q=java+message+digest&oq=java+message+digest&gs_l=psy-ab.3..35i39i19j0i203l9.6993.6993..7243...1.0..0.73.311.5......0....1..gws-wiz.......0i19j0i8i30i19j0i8i30.oMupp85VMko&ved=0ahUKEwja9rPF8efnAhVKwosBHVuHBoEQ4dUDCAs&uact=5",
				"https://admin.typeform.com/form/BYuRzZ/connect#/section/integrations",
				"https://github.com/kdn251/interviews#graph-algorithms",
				"https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/",
				"https://leetcode.com/discuss/interview-question/456379/amazon-sde-2-onsite-reject",
		};

		String foundLongUrl;

		// 第一次建立新紀錄 (DB 沒有任何記錄) 並找尋 longUrl
		for (String longUrl : longUrlList) {
			String tinyUrl = service.longUrlToTinyUrl(longUrl);
			System.out.println("Create tinyUrl >>> " + tinyUrl + " (1st time)");

			foundLongUrl = service.tinyUrlToLongUrl(tinyUrl);
			System.out.println("Find longUrl >>> " + foundLongUrl + "\n\n");
		}

		// 第二次以後，透過已經建立的舊紀錄找尋 longUrl
		for (String longUrl : longUrlList) {
			String tinyUrl = service.longUrlToTinyUrl(longUrl);
			System.out.println("Only find tinyUrl >>> " + tinyUrl);

			foundLongUrl = service.tinyUrlToLongUrl(tinyUrl);
			System.out.println("Find longUrl >>> " + foundLongUrl + "\n\n");
		}

		SpringApplication.run(TinyurlApplication.class, args);
	}

}
