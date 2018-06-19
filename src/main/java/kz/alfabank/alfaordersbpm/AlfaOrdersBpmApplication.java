package kz.alfabank.alfaordersbpm;

import kz.alfabank.alfaordersbpm.config.AlfrescoProperties;
import kz.alfabank.alfaordersbpm.config.AuditLogProperties;
import kz.alfabank.alfaordersbpm.config.BpmCrmProperties;
import kz.alfabank.alfaordersbpm.domain.repositories.CustomRepositoryImpl;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
@EnableRabbit
@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
@EnableConfigurationProperties({BpmCrmProperties.class, AlfrescoProperties.class, AuditLogProperties.class})
@EnableRetry
public class AlfaOrdersBpmApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlfaOrdersBpmApplication.class, args);
	}


	@Bean
	public Executor asyncExecutor() {
		int cores = Runtime.getRuntime().availableProcessors();
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(cores);
		executor.setMaxPoolSize(cores + 1);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(1);
		executor.setKeepAliveSeconds(500);
		executor.setQueueCapacity(500);
		executor.setAllowCoreThreadTimeOut(true);
		executor.setThreadNamePrefix("asyncExecutor-");
		executor.setThreadGroupName("orders");
		executor.initialize();
		return executor;
	}

	@Bean
	public Boolean disableSSLValidation() throws KeyManagementException, NoSuchAlgorithmException {
		final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		sslContext.init(null, new TrustManager[]{new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
				// Do nothing because of Clietn is always trusted.
			}
			@Override
			public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
				// Do nothing because of Clietn is always trusted.
			}
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		}}, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> hostname.equalsIgnoreCase(session.getPeerHost()));
		return true;
	}

}
