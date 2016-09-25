package com.zyx1011.mobilesafe002.entity;

public class NetAddressInfo {
	public String showapi_res_code;
	public String showapi_res_error;
	public ShowBody showapi_res_body;

	public static class ShowBody {
		public String city;
		public String name;
		public String num;
		public String prov;
		public String provCode;
		int ret_code;
		int type;
	}

}
