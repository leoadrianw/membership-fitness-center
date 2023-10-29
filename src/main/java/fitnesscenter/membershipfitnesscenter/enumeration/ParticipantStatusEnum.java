package fitnesscenter.membershipfitnesscenter.enumeration;


public enum ParticipantStatusEnum {
    TERDAFTAR {
        @Override
        public String toString() {
            return "TERDAFTAR";
        }
    },
    BELUM_TERVALIDASI {
        @Override
        public String toString() {
            return "BELUM TERVALIDASI";
        }
    },
    TIDAK_TERDAFTAR {
        @Override
        public String toString() {
            return "TIDAK TERDAFTAR";
        }
    }

}
