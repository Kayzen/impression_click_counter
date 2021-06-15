package com.kayzen.impcount.aerospike.api;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@ToString
public class AerospikeResponse {
	@Getter
	@SerializedName("success")
	private boolean success;
	@Getter
	@SerializedName("total-count")
	private int totalCount;
	@Getter
	@SerializedName("success-count")
	private int successCount;
	@Getter
	@SerializedName("error-count")
	private int errorCount;

	boolean isSuccessful(int sentRequestCount) {
		return (this.success) && (this.errorCount == 0) && (this.successCount == this.totalCount) && (sentRequestCount == this.totalCount);
	}
}
