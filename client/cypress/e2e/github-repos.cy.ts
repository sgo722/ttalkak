describe("GitHub 저장소 탐색 기능 E2E 테스트", () => {
  beforeEach(() => {
    // 저장소 목록 api 요청 모킹
    cy.intercept(
      "GET",
      "https://api.github.com/user/repos?type=public&per_page=100",
      {
        fixture: "repos.json",
      }
    ).as("getRepos");

    // 저장소 내용 api 요청 모킹
    cy.intercept("GET", "https://api.github.com/repos/**/**/contents/**", {
      fixture: "repo-contents.json",
    }).as("getRepoContents");

    // 테스트 시작 전 페이지로 이동
    cy.visit("http://localhost:3000/deploy");
  });

  it("저장소 목록이 표시되는지 테스트", () => {
    // api 요청이 끝날때까지 대기
    cy.wait("@getRepos");
    // 저장소 목록에 하나 이상의 항목이 있는지 확인
    cy.get('[data-cy="repo-list"] > [data-cy="repo-item"]').should(
      "have.length.greaterThan",
      0
    );
    // 첫 번째 저장소 항목을 선택하고 그 내부를 검사
    cy.get('[data-cy="repo-list"] > [data-cy="repo-item"]')
      .first()
      .within(() => {
        // 프로필 이미지가 표시되었는지 확인
        cy.get('[data-cy="repo-image"]').should("be.visible");
        // 저장소 이름에 'agricola'가 포함되었는지 확인
        cy.get('[data-cy="repo-name"]').should("contain.text", "agricola");
      });
  });

  it("저장소 검색 기능이 작동되는지 테스트", () => {
    // api 요청이 끝날때까지 대기
    cy.wait("@getRepos");
    // 검색할 단어 정의(테스트)
    const searchTerm = "agricola";
    // 검색 입력 필드에 검색어를 입력
    cy.get('[data-cy="repo-search"]').type(searchTerm);
    // 검색 결과로 표시된 저장소 항목이 정확히 1개인지 확인(여기선 더미데이터에 arricola를 1개만 넣어놔서)
    cy.get('[data-cy="repo-list"] > [data-cy="repo-item"]').should(
      "have.length",
      1
    );
    // 검색 결과의 첫 번째 항목에 'agricola'가 포함되어 있는지 확인
    cy.get('[data-cy="repo-list"] > [data-cy="repo-item"]')
      .first()
      .should("contain.text", "agricola");
  });

  it("저장소 내용이 올바르게 표시되는지 테스트", () => {
    // api 요청이 끝날때까지 대기
    cy.wait("@getRepos");
    // 첫 번째 저장소 클릭
    cy.get('[data-cy="repo-list"] > [data-cy="repo-item"]').first().click();
    // 저장소 내용 api 요청 대기
    cy.wait("@getRepoContents");
    // 디렉토리 네비게이터가 표시되는지 확인
    cy.get('[data-cy="directory-navigator"]').should("be.visible");
    // 파일 목록에 항목이 있는지 확인
    cy.get('[data-cy="file-list"] tbody > [data-cy="file-item"]').should(
      "have.length.greaterThan",
      0
    );
    // 첫 번째 파일 항목 확인
    cy.get('[data-cy="file-list"] tbody > [data-cy="file-item"]')
      .first()
      .within(() => {
        // 파일 이름이 표시되는지 확인
        cy.get('[data-cy="file-name"]').should("be.visible");
        // 커밋 메시지가 표시되는지 확인
        cy.get('[data-cy="file-commit-message"]').should("be.visible");
        // 커밋 날짜가 표시되는지 확인
        cy.get('[data-cy="file-commit-date"]').should("be.visible");
      });
  });
});
