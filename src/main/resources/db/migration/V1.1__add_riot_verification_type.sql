-- 컬럼이 없는 경우에만 추가
SET @col_exists = (SELECT COUNT(*)
                  FROM information_schema.columns
                  WHERE table_name = 'member'
                  AND column_name = 'riot_verification_type'
                  AND table_schema = DATABASE());

SET @sql = IF(@col_exists = 0,
              'ALTER TABLE member ADD COLUMN riot_verification_type VARCHAR(20) DEFAULT ''API_PARSED''',
              'SELECT ''riot_verification_type already exists'' AS status');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @col_exists = (SELECT COUNT(*)
                  FROM information_schema.columns
                  WHERE table_name = 'member'
                  AND column_name = 'riot_verified_at'
                  AND table_schema = DATABASE());

SET @sql = IF(@col_exists = 0,
              'ALTER TABLE member ADD COLUMN riot_verified_at TIMESTAMP',
              'SELECT ''riot_verified_at already exists'' AS status');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 기존 데이터 업데이트 (항상 실행)
UPDATE member
SET riot_verification_type = 'API_PARSED',
    riot_verified_at = NOW()
WHERE puuid IS NOT NULL
  AND puuid != ''
  AND (riot_verification_type IS NULL OR riot_verification_type = '');