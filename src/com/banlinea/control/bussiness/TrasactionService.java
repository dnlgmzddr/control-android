package com.banlinea.control.bussiness;

import android.content.Context;

import com.banlinea.control.remote.RemoteTransactionService;

public class TrasactionService extends BaseService {

	private RemoteTransactionService remoteTransactionService;

	public TrasactionService(Context context) {

		super(context);
		remoteTransactionService = new RemoteTransactionService();
	}
}
