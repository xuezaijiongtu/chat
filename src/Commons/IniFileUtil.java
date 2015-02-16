package Commons;

import java.io.File;
import java.io.IOException;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

/**
 * 配置文件操作
 * @author liuxing
 * @date 2015-01-20
 * 
 */
public class IniFileUtil {
	/**
	 * 加载文件
	 * 
	 * @param iniPath
	 * @throws IOException
	 * @throws InvalidFileFormatException
	 */
	public IniFileUtil(String iniPath) throws IOException {
		ini = new Ini(new File(iniPath));
	}

	Ini ini = null;

	/**
	 * 写INI文件
	 * 
	 * @param section
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void writeValue(String section, String key, String value) throws IOException {

		ini.put(section, key, value);
		ini.store();
	}

	/**
	 * 读取INI文件
	 * 
	 * @param section
	 * @param key
	 * @return
	 */
	public String readValue(String section, String key) {
		return ini.get(section, key);
	}

	/**
	 * 删除ini文件下personal段落下的所有键
	 * 
	 * @param Section
	 */
	public void ClearSection(String section) {
		ini.remove(section);
	}
}