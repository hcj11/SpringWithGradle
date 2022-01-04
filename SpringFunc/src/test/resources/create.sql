drop table  if exists "users";
CREATE TABLE "public"."users" (
                                  "id" int4 NOT NULL DEFAULT nextval( 'users_id_seq' :: regclass ),
                                  "user_group_id" int4,
                                  "name" VARCHAR ( 40 ) COLLATE "pg_catalog"."default",
                                  "age" int4,
                                  "create_time" TIMESTAMP ( 6 ) DEFAULT CURRENT_TIMESTAMP,
                                  "money" NUMERIC ( 13, 2 ),
                                  "user_account_id" int4,
                                  "user_group_name" VARCHAR ( 255 ) COLLATE "pg_catalog"."default",
                                  CONSTRAINT "users_pkey" PRIMARY KEY ( "id" )
);
ALTER TABLE "public"."users"
    OWNER TO "postgres";

CREATE UNIQUE INDEX "idx_group_name" ON "public"."users" USING btree (
  "user_group_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

CREATE UNIQUE INDEX "idx_name" ON "public"."users" USING btree (
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

COMMENT ON COLUMN "public"."users"."user_account_id" IS '账号id';