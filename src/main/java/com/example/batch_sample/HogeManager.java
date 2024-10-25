package com.example.batch_sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 〇〇共通部品の例
 */
@Component
public class HogeManager {
    private static final Logger logger =
            LoggerFactory.getLogger(HogeManager.class);
	
	/**
	 * 〇〇共通部品アプリケーション設定クラス
	 */
    @ConfigurationProperties("hoge-manager")
    public static record HogeManagerProperties(String hoge) {
    	
    }
    
	private final HogeManagerProperties properties;
	
	/**
	 * コンストラクタ
	 * @param properties
	 */
	public HogeManager(HogeManagerProperties properties) {
		this.properties = properties;
	}
	
	/**
	 * HOGEを表示
	 */
	public void showHoge() {
		logger.info(properties.hoge);// アプリケーション設定値の参照
	}

}
