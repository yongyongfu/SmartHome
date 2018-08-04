package com.demo.smarthome.protocol;

public enum MSGSTATE {// ����: 0x02 ȷ�ϻظ�:0x01 �񶨻ظ�:0x00

	MSG_SEND_ERROE((byte) 0x01), MSG_SEND_OK((byte) 0x00), MSG_SEND((byte) 0x02);
	private byte val;

	// ���췽��
	private MSGSTATE(byte val) {
		this.val = val;
	}

	public static MSGSTATE valueOf(int val) {
		switch (val) {
		case 0x00:
			return MSG_SEND_OK;
		case 0x01:
			return MSG_SEND_ERROE;
		case 0x02:
			return MSG_SEND;
		default:
			return null;
		}
	}

	// get set ����
	public byte val() {
		return val;
	}
}