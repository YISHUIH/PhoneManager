package com.feicui.DBManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * ֻ�������ݿ��ļ��ĸ���
 * 
 * @author Administrator
 * 
 */
public class CopyAssetsDB {

	public static void copyDB(Context context) {
		try {
			// �ȵõ�һ���ļ�·��
			// 1�����ļ���data/data/����/���ݿ���.db
			String path = Environment.getDataDirectory() + "/data/"
					+ context.getPackageName() + "/commonnum.db";
			// ����·��ȥ�������ݿ��ļ�
			File file = new File(path);
			if (!file.exists()) {// ���������
				// �½��ļ�
				file.createNewFile();
				// �õ�assets�ļ��µ����ݿ��ļ�������
				InputStream is = context.getAssets().open("db/commonnum.db");
				// �õ����ݿ��ļ��������
				FileOutputStream fos = new FileOutputStream(file);

				byte[] b = new byte[1024];

				int len = 0;
				while ((len = is.read(b)) != -1) {
					fos.write(b, 0, len);
				}
				fos.close();
				is.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
