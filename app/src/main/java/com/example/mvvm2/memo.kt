package com.example.mvvm2

/**
 *
 * 꼭 해야되는거
 *
 * 1. 상세뷰 다른 클릭 안되게
 *
 *
 * */


/**
 * 프래그먼트 라이프 사이클은 2종류
 * onCreate ~ onDestroy             ...1
 * onCreateView ~ onDestroyView     ...2
 *
 * UI 관련 작업은 무조건 2번에서 / 1번에서 하면 crash
 * */

/**
 *
 * Glide에서 context로 넘겨받을 경우 라이프사이클의 영향을 받기 때문에 이미지 가져오기 전 종료되면 꺠질 수 있음
 * 예외처리 해야함함
 *
 * */