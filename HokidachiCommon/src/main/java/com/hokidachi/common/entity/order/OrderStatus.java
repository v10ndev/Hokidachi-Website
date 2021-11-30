package com.hokidachi.common.entity.order;

public enum OrderStatus {
    NEW {
        @Override
        public String defaultDescription() {
            return "Lệnh được đặt bởi khách hàng";
        }

    },

    CANCELLED {
        @Override
        public String defaultDescription() {
            return "Đặt hàng đã bị từ chối";
        }
    },

    PROCESSING {
        @Override
        public String defaultDescription() {
            return "Đặt hàng đang được xử lý";
        }
    },

    PACKAGED {
        @Override
        public String defaultDescription() {
            return "Sản phẩm đã được đóng gói";
        }
    },

    PICKED {
        @Override
        public String defaultDescription() {
            return "Shipper chọn gói hàng";
        }
    },

    SHIPPING {
        @Override
        public String defaultDescription() {
            return "Người giao hàng đang cung cấp gói hàng";
        }
    },

    DELIVERED {
        @Override
        public String defaultDescription() {
            return "Khách hàng nhận được sản phẩm";
        }
    },

    RETURNED {
        @Override
        public String defaultDescription() {
            return "Sản phẩm đã được trả lại";
        }
    },

    PAID {
        @Override
        public String defaultDescription() {
            return "Khách hàng đã trả tiền Đặt hàng";
        }
    },

    RETURN_REQUESTED {
        @Override
        public String defaultDescription() {
            return "Khách hàng gửi yêu cầu mua lại mua hàng";
        }
    },

    REFUNDED {
        @Override
        public String defaultDescription() {
            return "Khách hàng đã được hoàn trả";
        }
    };

    public abstract String defaultDescription();
}
