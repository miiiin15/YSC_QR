# QR코드를 이용한 체온 등록 앱 (캡스톤 디자인)

## 프로젝트 개요

코로나19 방역 지침에 따라 체온 체크와 방문자 명단 관리가 필수적인 상황에서, **추가 장비 없이 휴대폰만으로 간편하게 체온을 등록하고 관리할 수 있는 앱**을 개발하였습니다. 기존의 수기 작성 방식의 비효율성을 개선하고, **자동으로 데이터를 목록화하고 통계화하여 관리의 편리성을 높이는 것**이 목표입니다.

## 주요 기능

### 1. **QR코드 생성 및 스캔**

- Zxing 라이브러리를 활용하여 QR코드를 생성하고 스캔할 수 있는 기능을 구현했습니다.
- 방문자는 QR코드를 통해 체온 정보와 기본 신상 정보를 쉽게 등록할 수 있으며, 관리자는 이를 즉시 확인하여 효율적으로 관리할 수 있습니다.

### 2. **데이터 통신 및 목록화**

- Volley 라이브러리를 사용하여 **서버와의 통신**을 통해 데이터를 주고받고, **실시간 목록화**가 가능합니다.
- 방문자의 정보를 별도의 수기 작업 없이 목록화하고, 체온 데이터를 누적하여 **자동 관리**할 수 있습니다.

### 3. **통계 및 시각화**

- MPAndroidChart 라이브러리를 사용하여, **누적된 데이터를 통계화하고 시각화**하여 보여주는 기능을 추가했습니다.
- 선택한 일자의 체온 변화, 고온 인구 비율, 인구 유동 수치를 확인할 수 있으며, 이를 통해 데이터의 변화를 한눈에 파악할 수 있습니다.

- ## 개발 동기 및 필요성

- 기존의 체온 체크 및 방역 관리는 대부분 **수기 명부**를 통해 이루어져 비효율적이며, 오탈자 발생 및 관리의 어려움이 있었습니다.
- 이에 QR코드를 통해 **비대면으로 체온을 기록하고, 자동으로 데이터를 관리**할 수 있는 앱을 고안하게 되었습니다.
- **개별 방문자가 아닌 시설 중심의 데이터 관리**가 가능하여, 특정 기간 동안의 방문자 체온을 추적하고 분석할 수 있는 점이 기존 시스템과의 차별점입니다.

## 기대 효과

- **별도의 장비 없이 체온 등록 및 관리**가 가능하여 초기 비용을 절감할 수 있습니다.
- 체온 기록 및 통계를 자동화하여, **관리자의 업무 부담을 줄이고 효율성을 높일 수 있습니다**.
- 데이터의 통계화 및 시각화를 통해 향후 방역 정책 수립이나 위험 지역 식별 등의 **확장된 활용**이 가능합니다.

## 기술 스택 및 라이브러리

- **Android Studio**: 앱 개발 환경
- **Zxing 라이브러리**: QR코드 생성 및 스캔 기능
- **Volley 라이브러리**: 서버와의 데이터 통신 및 송수신
- **MPAndroidChart**: 누적 데이터의 통계 및 시각화

## 개발 중 직면한 문제 및 해결 방안

1. **데이터베이스 관리의 어려움**
    - 기기 내 데이터만으로 관리하려다 보니 접근성과 확장성의 문제가 발생.
    - 해결: 별도의 서버와 DB를 구축하여, Volley 라이브러리로 데이터를 송수신하도록 수정.
2. **단순 QR코드 기능의 한계**
    - 기존 QR코드 앱과의 차별성을 두기 위해 추가적인 기능이 필요.
    - 해결: 체온 정보와 방문 이력을 자동으로 목록화하고, 통계 및 시각화 기능을 추가하여 데이터의 가시성을 높임.
